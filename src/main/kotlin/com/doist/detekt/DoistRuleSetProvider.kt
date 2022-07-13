package com.doist.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class DoistRuleSetProvider : RuleSetProvider {
    override val ruleSetId: String = "DoistRuleSet"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            ruleSetId,
            listOf(
                NoBlankNewLineAfterClassHeader(config),
            ),
        )
    }
}
