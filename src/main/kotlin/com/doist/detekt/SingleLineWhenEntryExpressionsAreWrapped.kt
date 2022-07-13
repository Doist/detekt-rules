package com.doist.detekt

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.com.intellij.psi.PsiWhiteSpace
import org.jetbrains.kotlin.psi.KtWhenEntry

class SingleLineWhenEntryExpressionsAreWrapped(config: Config) : Rule(config) {
    override val issue = Issue(
        javaClass.simpleName,
        Severity.Style,
        "Detects single line when entry expression without brackets",
        Debt.FIVE_MINS,
    )

    override fun visitWhenEntry(jetWhenEntry: KtWhenEntry) {
        super.visitWhenEntry(jetWhenEntry)

        val arrow = jetWhenEntry.arrow ?: return
        val whiteSpaceAfterArrow = arrow.nextSibling as? PsiWhiteSpace ?: return

        if (whiteSpaceAfterArrow.textContains('\n')) {
            val codeSmell = CodeSmell(
                issue = issue,
                entity = Entity.from(jetWhenEntry),
                message = "This when entry expression should be wrapped with brackets",
            )
            report(codeSmell)
        }
    }
}
