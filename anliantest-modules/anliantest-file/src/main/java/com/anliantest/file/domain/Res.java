package com.anliantest.file.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
 
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Res implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer code;
    private Object data = "";
    private String message = "";
}