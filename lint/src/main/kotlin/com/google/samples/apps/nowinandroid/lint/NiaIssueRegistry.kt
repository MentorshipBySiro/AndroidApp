package com.swahilib.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.swahilib.lint.designsystem.DesignSystemDetector

class AppIssueRegistry : IssueRegistry() {

    override val issues = listOf(
        DesignSystemDetector.ISSUE,
        TestMethodNameDetector.FORMAT,
        TestMethodNameDetector.PREFIX,
    )

    override val api: Int = CURRENT_API

    override val minApi: Int = 12

    override val vendor: Vendor = Vendor(
        vendorName = "SwahiLib",
        feedbackUrl = "https://github.com/SiroDevs/SwahiLib-Android/issues",
        contact = "https://github.com/SiroDevs/SwahiLib-Android",
    )
}
