package core.schema;

import core.util.Cell;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 附录
 */
@Setter
@Getter
public class Appendix extends Node{

    List<Cell<String>> cells = new ArrayList<>();

    public boolean isEmpty() {
        return cells.isEmpty();
    }

}
