package com.swahilib.core.data.repository

import com.swahilib.core.data.Synchronizer
import com.swahilib.core.data.model.asEntity
import com.swahilib.core.data.model.topicCrossReferences
import com.swahilib.core.data.model.topicEntityShells
import com.swahilib.core.data.testdoubles.CollectionType
import com.swahilib.core.data.testdoubles.TestNewsResourceDao
import com.swahilib.core.data.testdoubles.TestAppNetworkDataSource
import com.swahilib.core.data.testdoubles.TestTopicDao
import com.swahilib.core.data.testdoubles.filteredInterestsIds
import com.swahilib.core.data.testdoubles.nonPresentInterestsIds
import com.swahilib.core.database.model.NewsResourceEntity
import com.swahilib.core.database.model.NewsResourceTopicCrossRef
import com.swahilib.core.database.model.PopulatedNewsResource
import com.swahilib.core.database.model.TopicEntity
import com.swahilib.core.database.model.asExternalModel
import com.swahilib.core.datastore.AppPreferencesDataSource
import com.swahilib.core.datastore.UserPreferences
import com.swahilib.core.datastore.test.InMemoryDataStore
import com.swahilib.core.model.data.NewsResource
import com.swahilib.core.model.data.Topic
import com.swahilib.core.network.model.NetworkChangeList
import com.swahilib.core.network.model.NetworkNewsResource
import com.swahilib.core.testing.notifications.TestNotifier
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OfflineFirstNewsRepositoryTest {

    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: OfflineFirstNewsRepository

    private lateinit var appPreferencesDataSource: AppPreferencesDataSource

    private lateinit var newsResourceDao: TestNewsResourceDao

    private lateinit var topicDao: TestTopicDao

    private lateinit var network: TestAppNetworkDataSource

    private lateinit var notifier: TestNotifier

    private lateinit var synchronizer: Synchronizer

    @Before
    fun setup() {
        appPreferencesDataSource = AppPreferencesDataSource(InMemoryDataStore(UserPreferences.getDefaultInstance()))
        newsResourceDao = TestNewsResourceDao()
        topicDao = TestTopicDao()
        network = TestAppNetworkDataSource()
        notifier = TestNotifier()
        synchronizer = TestSynchronizer(
            appPreferencesDataSource,
        )

        subject = OfflineFirstNewsRepository(
            appPreferencesDataSource = appPreferencesDataSource,
            newsResourceDao = newsResourceDao,
            topicDao = topicDao,
            network = network,
            notifier = notifier,
        )
    }

    @Test
    fun offlineFirstNewsRepository_news_resources_stream_is_backed_by_news_resource_dao() =
        testScope.runTest {
            subject.syncWith(synchronizer)
            assertEquals(
                newsResourceDao.getNewsResources()
                    .first()
                    .map(PopulatedNewsResource::asExternalModel),
                subject.getNewsResources()
                    .first(),
            )
        }

    @Test
    fun offlineFirstNewsRepository_news_resources_for_topic_is_backed_by_news_resource_dao() =
        testScope.runTest {
            assertEquals(
                expected = newsResourceDao.getNewsResources(
                    filterTopicIds = filteredInterestsIds,
                    useFilterTopicIds = true,
                )
                    .first()
                    .map(PopulatedNewsResource::asExternalModel),
                actual = subject.getNewsResources(
                    query = NewsResourceQuery(
                        filterTopicIds = filteredInterestsIds,
                    ),
                )
                    .first(),
            )

            assertEquals(
                expected = emptyList(),
                actual = subject.getNewsResources(
                    query = NewsResourceQuery(
                        filterTopicIds = nonPresentInterestsIds,
                    ),
                )
                    .first(),
            )
        }

    @Test
    fun offlineFirstNewsRepository_sync_pulls_from_network() =
        testScope.runTest {
            // User has not onboarded
            appPreferencesDataSource.setShouldHideOnboarding(false)
            subject.syncWith(synchronizer)

            val newsResourcesFromNetwork = network.getNewsResources()
                .map(NetworkNewsResource::asEntity)
                .map(NewsResourceEntity::asExternalModel)

            val newsResourcesFromDb = newsResourceDao.getNewsResources()
                .first()
                .map(PopulatedNewsResource::asExternalModel)

            assertEquals(
                newsResourcesFromNetwork.map(NewsResource::id).sorted(),
                newsResourcesFromDb.map(NewsResource::id).sorted(),
            )

            // After sync version should be updated
            assertEquals(
                expected = network.latestChangeListVersion(CollectionType.NewsResources),
                actual = synchronizer.getChangeListVersions().newsResourceVersion,
            )

            // Notifier should not have been called
            assertTrue(notifier.addedNewsResources.isEmpty())
        }

    @Test
    fun offlineFirstNewsRepository_sync_deletes_items_marked_deleted_on_network() =
        testScope.runTest {
            // User has not onboarded
            appPreferencesDataSource.setShouldHideOnboarding(false)

            val newsResourcesFromNetwork = network.getNewsResources()
                .map(NetworkNewsResource::asEntity)
                .map(NewsResourceEntity::asExternalModel)

            // Delete half of the items on the network
            val deletedItems = newsResourcesFromNetwork
                .map(NewsResource::id)
                .partition { it.chars().sum() % 2 == 0 }
                .first
                .toSet()

            deletedItems.forEach {
                network.editCollection(
                    collectionType = CollectionType.NewsResources,
                    id = it,
                    isDelete = true,
                )
            }

            subject.syncWith(synchronizer)

            val newsResourcesFromDb = newsResourceDao.getNewsResources()
                .first()
                .map(PopulatedNewsResource::asExternalModel)

            // Assert that items marked deleted on the network have been deleted locally
            assertEquals(
                expected = (newsResourcesFromNetwork.map(NewsResource::id) - deletedItems).sorted(),
                actual = newsResourcesFromDb.map(NewsResource::id).sorted(),
            )

            // After sync version should be updated
            assertEquals(
                expected = network.latestChangeListVersion(CollectionType.NewsResources),
                actual = synchronizer.getChangeListVersions().newsResourceVersion,
            )

            // Notifier should not have been called
            assertTrue(notifier.addedNewsResources.isEmpty())
        }

    @Test
    fun offlineFirstNewsRepository_incremental_sync_pulls_from_network() =
        testScope.runTest {
            // User has not onboarded
            appPreferencesDataSource.setShouldHideOnboarding(false)

            // Set news version to 7
            synchronizer.updateChangeListVersions {
                copy(newsResourceVersion = 7)
            }

            subject.syncWith(synchronizer)

            val changeList = network.changeListsAfter(
                CollectionType.NewsResources,
                version = 7,
            )
            val changeListIds = changeList
                .map(NetworkChangeList::id)
                .toSet()

            val newsResourcesFromNetwork = network.getNewsResources()
                .map(NetworkNewsResource::asEntity)
                .map(NewsResourceEntity::asExternalModel)
                .filter { it.id in changeListIds }

            val newsResourcesFromDb = newsResourceDao.getNewsResources()
                .first()
                .map(PopulatedNewsResource::asExternalModel)

            assertEquals(
                expected = newsResourcesFromNetwork.map(NewsResource::id).sorted(),
                actual = newsResourcesFromDb.map(NewsResource::id).sorted(),
            )

            // After sync version should be updated
            assertEquals(
                expected = changeList.last().changeListVersion,
                actual = synchronizer.getChangeListVersions().newsResourceVersion,
            )

            // Notifier should not have been called
            assertTrue(notifier.addedNewsResources.isEmpty())
        }

    @Test
    fun offlineFirstNewsRepository_sync_saves_shell_topic_entities() =
        testScope.runTest {
            subject.syncWith(synchronizer)

            assertEquals(
                expected = network.getNewsResources()
                    .map(NetworkNewsResource::topicEntityShells)
                    .flatten()
                    .distinctBy(TopicEntity::id)
                    .sortedBy(TopicEntity::toString),
                actual = topicDao.getTopicEntities()
                    .first()
                    .sortedBy(TopicEntity::toString),
            )
        }

    @Test
    fun offlineFirstNewsRepository_sync_saves_topic_cross_references() =
        testScope.runTest {
            subject.syncWith(synchronizer)

            assertEquals(
                expected = network.getNewsResources()
                    .map(NetworkNewsResource::topicCrossReferences)
                    .flatten()
                    .distinct()
                    .sortedBy(NewsResourceTopicCrossRef::toString),
                actual = newsResourceDao.topicCrossReferences
                    .sortedBy(NewsResourceTopicCrossRef::toString),
            )
        }

    @Test
    fun offlineFirstNewsRepository_sync_marks_as_read_on_first_run() =
        testScope.runTest {
            subject.syncWith(synchronizer)

            assertEquals(
                network.getNewsResources().map { it.id }.toSet(),
                appPreferencesDataSource.userData.first().viewedNewsResources,
            )
        }

    @Test
    fun offlineFirstNewsRepository_sync_does_not_mark_as_read_on_subsequent_run() =
        testScope.runTest {
            // Pretend that we already have up to change list 7
            synchronizer.updateChangeListVersions {
                copy(newsResourceVersion = 7)
            }

            subject.syncWith(synchronizer)

            assertEquals(
                emptySet(),
                appPreferencesDataSource.userData.first().viewedNewsResources,
            )
        }

    @Test
    fun offlineFirstNewsRepository_sends_notifications_for_newly_synced_news_that_is_followed() =
        testScope.runTest {
            // User has onboarded
            appPreferencesDataSource.setShouldHideOnboarding(true)

            val networkNewsResources = network.getNewsResources()

            // Follow roughly half the topics
            val followedTopicIds = networkNewsResources
                .flatMap(NetworkNewsResource::topicEntityShells)
                .mapNotNull { topic ->
                    when (topic.id.chars().sum() % 2) {
                        0 -> topic.id
                        else -> null
                    }
                }
                .toSet()

            // Set followed topics
            appPreferencesDataSource.setFollowedTopicIds(followedTopicIds)

            subject.syncWith(synchronizer)

            val followedNewsResourceIdsFromNetwork = networkNewsResources
                .filter { (it.topics intersect followedTopicIds).isNotEmpty() }
                .map(NetworkNewsResource::id)
                .sorted()

            // Notifier should have been called with only news resources that have topics
            // that the user follows
            assertEquals(
                expected = followedNewsResourceIdsFromNetwork,
                actual = notifier.addedNewsResources.first().map(NewsResource::id).sorted(),
            )
        }

    @Test
    fun offlineFirstNewsRepository_does_not_send_notifications_for_existing_news_resources() =
        testScope.runTest {
            // User has onboarded
            appPreferencesDataSource.setShouldHideOnboarding(true)

            val networkNewsResources = network.getNewsResources()
                .map(NetworkNewsResource::asEntity)

            val newsResources = networkNewsResources
                .map(NewsResourceEntity::asExternalModel)

            // Prepopulate dao with news resources
            newsResourceDao.upsertNewsResources(networkNewsResources)

            val followedTopicIds = newsResources
                .flatMap(NewsResource::topics)
                .map(Topic::id)
                .toSet()

            // Follow all topics
            appPreferencesDataSource.setFollowedTopicIds(followedTopicIds)

            subject.syncWith(synchronizer)

            // Notifier should not have been called bc all news resources existed previously
            assertTrue(notifier.addedNewsResources.isEmpty())
        }
}
