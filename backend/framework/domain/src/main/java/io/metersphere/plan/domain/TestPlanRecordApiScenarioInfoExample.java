package io.metersphere.plan.domain;

import java.util.ArrayList;
import java.util.List;

public class TestPlanRecordApiScenarioInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TestPlanRecordApiScenarioInfoExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(String value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("id like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("id not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andTestPlanExecuteRecordIdIsNull() {
            addCriterion("test_plan_execute_record_id is null");
            return (Criteria) this;
        }

        public Criteria andTestPlanExecuteRecordIdIsNotNull() {
            addCriterion("test_plan_execute_record_id is not null");
            return (Criteria) this;
        }

        public Criteria andTestPlanExecuteRecordIdEqualTo(String value) {
            addCriterion("test_plan_execute_record_id =", value, "testPlanExecuteRecordId");
            return (Criteria) this;
        }

        public Criteria andTestPlanExecuteRecordIdNotEqualTo(String value) {
            addCriterion("test_plan_execute_record_id <>", value, "testPlanExecuteRecordId");
            return (Criteria) this;
        }

        public Criteria andTestPlanExecuteRecordIdGreaterThan(String value) {
            addCriterion("test_plan_execute_record_id >", value, "testPlanExecuteRecordId");
            return (Criteria) this;
        }

        public Criteria andTestPlanExecuteRecordIdGreaterThanOrEqualTo(String value) {
            addCriterion("test_plan_execute_record_id >=", value, "testPlanExecuteRecordId");
            return (Criteria) this;
        }

        public Criteria andTestPlanExecuteRecordIdLessThan(String value) {
            addCriterion("test_plan_execute_record_id <", value, "testPlanExecuteRecordId");
            return (Criteria) this;
        }

        public Criteria andTestPlanExecuteRecordIdLessThanOrEqualTo(String value) {
            addCriterion("test_plan_execute_record_id <=", value, "testPlanExecuteRecordId");
            return (Criteria) this;
        }

        public Criteria andTestPlanExecuteRecordIdLike(String value) {
            addCriterion("test_plan_execute_record_id like", value, "testPlanExecuteRecordId");
            return (Criteria) this;
        }

        public Criteria andTestPlanExecuteRecordIdNotLike(String value) {
            addCriterion("test_plan_execute_record_id not like", value, "testPlanExecuteRecordId");
            return (Criteria) this;
        }

        public Criteria andTestPlanExecuteRecordIdIn(List<String> values) {
            addCriterion("test_plan_execute_record_id in", values, "testPlanExecuteRecordId");
            return (Criteria) this;
        }

        public Criteria andTestPlanExecuteRecordIdNotIn(List<String> values) {
            addCriterion("test_plan_execute_record_id not in", values, "testPlanExecuteRecordId");
            return (Criteria) this;
        }

        public Criteria andTestPlanExecuteRecordIdBetween(String value1, String value2) {
            addCriterion("test_plan_execute_record_id between", value1, value2, "testPlanExecuteRecordId");
            return (Criteria) this;
        }

        public Criteria andTestPlanExecuteRecordIdNotBetween(String value1, String value2) {
            addCriterion("test_plan_execute_record_id not between", value1, value2, "testPlanExecuteRecordId");
            return (Criteria) this;
        }

        public Criteria andTestPlanApiScenarioIdIsNull() {
            addCriterion("test_plan_api_scenario_id is null");
            return (Criteria) this;
        }

        public Criteria andTestPlanApiScenarioIdIsNotNull() {
            addCriterion("test_plan_api_scenario_id is not null");
            return (Criteria) this;
        }

        public Criteria andTestPlanApiScenarioIdEqualTo(String value) {
            addCriterion("test_plan_api_scenario_id =", value, "testPlanApiScenarioId");
            return (Criteria) this;
        }

        public Criteria andTestPlanApiScenarioIdNotEqualTo(String value) {
            addCriterion("test_plan_api_scenario_id <>", value, "testPlanApiScenarioId");
            return (Criteria) this;
        }

        public Criteria andTestPlanApiScenarioIdGreaterThan(String value) {
            addCriterion("test_plan_api_scenario_id >", value, "testPlanApiScenarioId");
            return (Criteria) this;
        }

        public Criteria andTestPlanApiScenarioIdGreaterThanOrEqualTo(String value) {
            addCriterion("test_plan_api_scenario_id >=", value, "testPlanApiScenarioId");
            return (Criteria) this;
        }

        public Criteria andTestPlanApiScenarioIdLessThan(String value) {
            addCriterion("test_plan_api_scenario_id <", value, "testPlanApiScenarioId");
            return (Criteria) this;
        }

        public Criteria andTestPlanApiScenarioIdLessThanOrEqualTo(String value) {
            addCriterion("test_plan_api_scenario_id <=", value, "testPlanApiScenarioId");
            return (Criteria) this;
        }

        public Criteria andTestPlanApiScenarioIdLike(String value) {
            addCriterion("test_plan_api_scenario_id like", value, "testPlanApiScenarioId");
            return (Criteria) this;
        }

        public Criteria andTestPlanApiScenarioIdNotLike(String value) {
            addCriterion("test_plan_api_scenario_id not like", value, "testPlanApiScenarioId");
            return (Criteria) this;
        }

        public Criteria andTestPlanApiScenarioIdIn(List<String> values) {
            addCriterion("test_plan_api_scenario_id in", values, "testPlanApiScenarioId");
            return (Criteria) this;
        }

        public Criteria andTestPlanApiScenarioIdNotIn(List<String> values) {
            addCriterion("test_plan_api_scenario_id not in", values, "testPlanApiScenarioId");
            return (Criteria) this;
        }

        public Criteria andTestPlanApiScenarioIdBetween(String value1, String value2) {
            addCriterion("test_plan_api_scenario_id between", value1, value2, "testPlanApiScenarioId");
            return (Criteria) this;
        }

        public Criteria andTestPlanApiScenarioIdNotBetween(String value1, String value2) {
            addCriterion("test_plan_api_scenario_id not between", value1, value2, "testPlanApiScenarioId");
            return (Criteria) this;
        }

        public Criteria andReportIdIsNull() {
            addCriterion("report_id is null");
            return (Criteria) this;
        }

        public Criteria andReportIdIsNotNull() {
            addCriterion("report_id is not null");
            return (Criteria) this;
        }

        public Criteria andReportIdEqualTo(String value) {
            addCriterion("report_id =", value, "reportId");
            return (Criteria) this;
        }

        public Criteria andReportIdNotEqualTo(String value) {
            addCriterion("report_id <>", value, "reportId");
            return (Criteria) this;
        }

        public Criteria andReportIdGreaterThan(String value) {
            addCriterion("report_id >", value, "reportId");
            return (Criteria) this;
        }

        public Criteria andReportIdGreaterThanOrEqualTo(String value) {
            addCriterion("report_id >=", value, "reportId");
            return (Criteria) this;
        }

        public Criteria andReportIdLessThan(String value) {
            addCriterion("report_id <", value, "reportId");
            return (Criteria) this;
        }

        public Criteria andReportIdLessThanOrEqualTo(String value) {
            addCriterion("report_id <=", value, "reportId");
            return (Criteria) this;
        }

        public Criteria andReportIdLike(String value) {
            addCriterion("report_id like", value, "reportId");
            return (Criteria) this;
        }

        public Criteria andReportIdNotLike(String value) {
            addCriterion("report_id not like", value, "reportId");
            return (Criteria) this;
        }

        public Criteria andReportIdIn(List<String> values) {
            addCriterion("report_id in", values, "reportId");
            return (Criteria) this;
        }

        public Criteria andReportIdNotIn(List<String> values) {
            addCriterion("report_id not in", values, "reportId");
            return (Criteria) this;
        }

        public Criteria andReportIdBetween(String value1, String value2) {
            addCriterion("report_id between", value1, value2, "reportId");
            return (Criteria) this;
        }

        public Criteria andReportIdNotBetween(String value1, String value2) {
            addCriterion("report_id not between", value1, value2, "reportId");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}