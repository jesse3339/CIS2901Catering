package jesse.rossetti.SQL.interfaces;

import java.sql.Connection;
import java.sql.Statement;

public interface IEntity<T> {
    void addToTable(Connection conn);
}
