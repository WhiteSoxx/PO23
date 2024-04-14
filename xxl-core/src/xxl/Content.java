package xxl;
import java.util.List;

import xxl.Types.Cell;
import java.io.Serializable;

public interface Content extends Serializable{
    String operator(List<Cell> list);
}
