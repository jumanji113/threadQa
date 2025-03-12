package models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class People {
    private String name;
    private Integer age;
    private String sex;
}
