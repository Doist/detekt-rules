package com.doist.detekt

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.com.intellij.psi.PsiWhiteSpace
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtClassBody

class NoBlankNewLineAfterClassHeader(config: Config) : Rule(config) {
    override val issue = Issue(
        javaClass.simpleName,
        Severity.Style,
        "Detects blank line after class header",
        Debt.FIVE_MINS,
    )

    override fun visitClassBody(classBody: KtClassBody) {
        super.visitClassBody(classBody)

        val lBrace = classBody.lBrace ?: return
        val whiteSpaceAfterLBrace = lBrace.nextSibling as? PsiWhiteSpace ?: return

        if (whiteSpaceAfterLBrace.text.count { it == '\n' } > 1) {
            val codeSmell = CodeSmell(
                issue = issue,
                entity = Entity.from(whiteSpaceAfterLBrace),
                message = "Unnecessary blank line after the header",
            )
            report(codeSmell)
        }
    }
}
