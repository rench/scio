package com.scio.cloud.sentinel.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

@Configuration
public class SentinelFlowRuleConfig implements ApplicationContextAware {
  private static final Logger LOG = LoggerFactory.getLogger(SentinelFlowRuleConfig.class);

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.config();
  }

  private void config() {
    List<FlowRule> rules = new ArrayList<FlowRule>();
    FlowRule rule = new FlowRule();
    rule.setResource("/num");
    // set limit qps to 1
    rule.setCount(1);
    rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
    rule.setLimitApp("default");
    rules.add(rule);
    FlowRuleManager.loadRules(rules);
    LOG.info("init custom flow rule");
  }
}
