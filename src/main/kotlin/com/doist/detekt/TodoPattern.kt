package com.doist.detekt

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.api.config
import io.gitlab.arturbosch.detekt.api.internal.Configuration
import org.jetbrains.kotlin.com.intellij.psi.PsiComment
import org.jetbrains.kotlin.psi.KtPostfixExpression
import java.awt.SystemColor.text

class TodoPattern(config: Config) : Rule(config) {
    override val issue = Issue(
        javaClass.simpleName,
        Severity.Maintainability,
        "TODO comments should match the pattern set in the configuration.",
        Debt.FIVE_MINS,
    )

    @Configuration("TODO pattern")
    private val pattern: Regex by config(".*", String::toRegex)

    private val genericTodoCommentPattern = Regex("// todo[^a-zA-Z0-9].*", RegexOption.IGNORE_CASE)

    override fun visitComment(comment: PsiComment) {
        super.visitComment(comment)
        val text = comment.text

        if (!text.matches(genericTodoCommentPattern)) return

        if (!comment.text.matches(pattern)) {
            val codeSmell = CodeSmell(
                issue = issue,
                entity = Entity.from(comment),
                message = "TODO comment should match the pattern: $pattern",
            )
            report(codeSmell)
        }
    }

    companion object {
        const val PATTERN = "pattern"
    }
}
