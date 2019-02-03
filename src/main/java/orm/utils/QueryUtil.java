package orm.utils;

public class QueryUtil {
    public static String RemoveCommaFromQueryEnd(String query) {
        if (query.trim().endsWith(",")) {
            query = query.substring(0, query.length() - 1);
        }
        return query;
    }
}
