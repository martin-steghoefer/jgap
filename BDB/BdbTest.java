import com.sleepycat.db.*;

public class BdbTest
{
    public static void main(String args[])
    {
	try
	{
	    System.out.println("Testing BDB");
	    String dir = "./tmp";
	    // environment is transactional
	    int envFlags = Db.DB_CREATE | Db.DB_INIT_MPOOL;
	    int dbFlags = Db.DB_CREATE;
	    DbEnv env = new DbEnv(0);
	    env.open(dir, envFlags, 0);
	    Db db = new Db(env, 0);
	    db.open(null, "data.db", null, Db.DB_BTREE, dbFlags, 0);
	}
	catch(Exception e)
        {
	    System.out.println("Some Exception");
	    e.printStackTrace();
	}
    }
}
