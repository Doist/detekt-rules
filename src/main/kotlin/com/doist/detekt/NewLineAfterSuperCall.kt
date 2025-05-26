package com.doist.detekt

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtNamedFunction

class NewLineAfterSuperCall(config: Config = Config.empty) : Rule(config) {
    override val issue = Issue(
        javaClass.simpleName,
        Severity.Style,
        "Ensure there is a new line after every call to a super() method.",
        Debt.FIVE_MINS
    )

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)

        val statements = function.bodyBlockExpression?.statements

        statements?.forEachIndexed { index, statement ->
            if (statement.text?.startsWith("super.") == true) {
                // Don't report if this is the last statement
                if (index == statements.lastIndex) return@forEachIndexed
                
                if (statement.nextSibling.text.contains("\n\n")) return@forEachIndexed
                report(
                    CodeSmell(
                        issue,
                        Entity.from(statement),
                        "There should be a new line after the call to super()."
                    )
                )
            }
        }
    }
}
