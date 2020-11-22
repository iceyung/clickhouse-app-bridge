package iceyung.app.utils;

import ru.yandex.clickhouse.util.ClickHouseRowBinaryStream;

@FunctionalInterface
public interface OperatorUtil<T> {

    Object operator(T t, ClickHouseRowBinaryStream stream) throws Exception;
}
