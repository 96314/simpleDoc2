package simple.core.schema;

import lombok.*;

/**
 * 字段描述
 */
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Row {

    String key;
    String type;
    String condition;
    String def;
    String remark;

    public Row(String type) {
        this.type = type;
    }

}
