package org.squashtest.tf.gittest.domain;

public class TestCaseDto {

    private String name;
    private String gherkinScript;

    public TestCaseDto(String name, String gherkinScript) {
        this.name = name;
        this.gherkinScript = gherkinScript;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getGherkinScript() {
        return gherkinScript;
    }
    public void setGherkinScript(String gherkinScript) {
        this.gherkinScript = gherkinScript;
    }
}
