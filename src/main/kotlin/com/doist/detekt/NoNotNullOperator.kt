package com.doist.detekt

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtPostfixExpression

class NoNotNullOperator(config: Config) : Rule(config) {
    override val issue = Issue(
        javaClass.simpleName,
        Severity.Style,
        "Detects !! operator",
        Debt.FIVE_MINS,
    )

    override fun visitPostfixExpression(expression: KtPostfixExpression) {
        super.visitPostfixExpression(expression)

        if (expression.text.endsWith("!!")) {
            val codeSmell = CodeSmell(
                issue = issue,
                entity = Entity.from(expression),
                message = "Use requireNotNull instead of !! operator",
            )
            report(codeSmell)
        }
    }
}
