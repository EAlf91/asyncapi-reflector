package de.otto.asyncapidemo2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Another payload model")
@Getter
public class AnotherPayloadDto {

    @Schema(description = "Foo field", example = "bar")
    private String foo;

    @Schema(description = "Example field", required = true)
    private ExamplePayloadDto example;

    public String getFoo() {
        return foo;
    }

    @Schema(description = "Some enum field", example = "FOO2", required = true)
    private AnotherPayloadDto.Events someEnum;

    public void setFoo(String foo) {
        this.foo = foo;
    }

    public ExamplePayloadDto getExample() {
        return example;
    }

    public void setExample(ExamplePayloadDto example) {
        this.example = example;
    }

    public AnotherPayloadDto.Events getSomeEnum() {
        return someEnum;
    }
    enum Events {
        EVENT_1,
        EVENT_2
    }

    @Override
    public String toString() {
        return "AnotherPayloadDto{" +
                "foo='" + foo + '\'' +
                ", example=" + example +
                '}';
    }

}
