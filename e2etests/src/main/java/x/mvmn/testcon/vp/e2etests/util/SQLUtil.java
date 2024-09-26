package x.mvmn.testcon.vp.e2etests.util;

import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class SQLUtil {
    public static List<List<String>> convertResultSetToList(ResultSet resultSet) throws SQLException {
        List<List<String>> result = new ArrayList<>();

        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        List<String> columnNames = new ArrayList<>();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }
        result.add(columnNames);

        while (resultSet.next()) {
            List<String> values = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnValue = resultSet.getString(i);
                values.add(columnValue != null ? columnValue : "NULL");
            }
            result.add(values);
        }

        return result;
    }
}
