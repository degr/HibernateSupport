package org.forweb.database;

import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.function.VarArgsSQLFunction;
import org.hibernate.type.StandardBasicTypes;

import java.sql.Types;


/**
 * Created by rsmirnou on 10/6/2015. 22
 */
public class MySqlDialect extends MySQLDialect {
    public MySqlDialect() {
        super();
        registerColumnType( Types.NUMERIC, "decimal" );
        registerColumnType( Types.BIGINT, "decimal" );
        registerColumnType( Types.BIT, "tinyint" );

        registerFunction("TIMEDIFF", new VarArgsSQLFunction(StandardBasicTypes.INTEGER, "TIME_TO_SEC(timediff(", ",", "))"));
        registerFunction("GETDATE", new VarArgsSQLFunction(StandardBasicTypes.DATE, "now(", "", ")"));
    }
}