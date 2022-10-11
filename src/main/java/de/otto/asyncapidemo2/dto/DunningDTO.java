package de.otto.asyncapidemo2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Another payload model")
public class DunningDTO {

    @Schema(description = "Foo field", example = "bar")
    private String foo;

}
