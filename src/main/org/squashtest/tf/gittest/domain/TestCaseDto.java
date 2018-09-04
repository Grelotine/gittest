package org.squashtest.tf.gittest.domain;

public class TestCaseDto {

    private Long id;
    private String name;
    private String gherkinScript;

    public TestCaseDto(Long id, String name, String gherkinScript) {
        this.id = id;
        this.name = name;
        this.gherkinScript = gherkinScript;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
