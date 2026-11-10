package kr.co.oadan.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class OadanDatabase_Impl extends OadanDatabase {
  private volatile FastSessionDao _fastSessionDao;

  private volatile WaterLogDao _waterLogDao;

  private volatile ConditionLogDao _conditionLogDao;

  private volatile MealLogDao _mealLogDao;

  private volatile RecipeDao _recipeDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `fast_sessions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `startTimeUtc` INTEGER NOT NULL, `targetEndTimeUtc` INTEGER NOT NULL, `actualEndTimeUtc` INTEGER, `protocol` TEXT NOT NULL, `targetHours` REAL NOT NULL, `fastedHours` REAL, `isCompleted` INTEGER NOT NULL, `isPartiallyCompleted` INTEGER NOT NULL, `date` TEXT NOT NULL, `notes` TEXT, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `water_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestampUtc` INTEGER NOT NULL, `amountMl` INTEGER NOT NULL, `date` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `condition_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestampUtc` INTEGER NOT NULL, `date` TEXT NOT NULL, `weightKg` REAL, `skeletalMuscleKg` REAL, `bodyFatPercent` REAL, `conditionTag` TEXT, `memo` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `meal_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `fastSessionId` INTEGER, `timestampUtc` INTEGER NOT NULL, `date` TEXT NOT NULL, `menuName` TEXT NOT NULL, `photoUri` TEXT, `checkVegFirst` INTEGER NOT NULL, `checkNoProcessed` INTEGER NOT NULL, `checkSlow20m` INTEGER NOT NULL, `checkNoSweetDrink` INTEGER NOT NULL, `checkSeasonal` INTEGER NOT NULL, `organicScore` INTEGER NOT NULL, `organicGrade` TEXT NOT NULL, `feedbackMessage` TEXT NOT NULL, `notes` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `recipes` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `subtitle` TEXT NOT NULL, `stage` TEXT NOT NULL, `emoji` TEXT NOT NULL, `prepTimeMinutes` INTEGER NOT NULL, `glycemicFriendly` TEXT NOT NULL, `tags` TEXT NOT NULL, `eatOrderGuide` TEXT NOT NULL, `ingredients` TEXT NOT NULL, `instructions` TEXT NOT NULL, `seasonInfo` TEXT, `isBookmarked` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'e6fec75a703af0858b1b24d386a2b5db')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `fast_sessions`");
        db.execSQL("DROP TABLE IF EXISTS `water_logs`");
        db.execSQL("DROP TABLE IF EXISTS `condition_logs`");
        db.execSQL("DROP TABLE IF EXISTS `meal_logs`");
        db.execSQL("DROP TABLE IF EXISTS `recipes`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsFastSessions = new HashMap<String, TableInfo.Column>(12);
        _columnsFastSessions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFastSessions.put("startTimeUtc", new TableInfo.Column("startTimeUtc", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFastSessions.put("targetEndTimeUtc", new TableInfo.Column("targetEndTimeUtc", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFastSessions.put("actualEndTimeUtc", new TableInfo.Column("actualEndTimeUtc", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFastSessions.put("protocol", new TableInfo.Column("protocol", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFastSessions.put("targetHours", new TableInfo.Column("targetHours", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFastSessions.put("fastedHours", new TableInfo.Column("fastedHours", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFastSessions.put("isCompleted", new TableInfo.Column("isCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFastSessions.put("isPartiallyCompleted", new TableInfo.Column("isPartiallyCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFastSessions.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFastSessions.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFastSessions.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFastSessions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFastSessions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFastSessions = new TableInfo("fast_sessions", _columnsFastSessions, _foreignKeysFastSessions, _indicesFastSessions);
        final TableInfo _existingFastSessions = TableInfo.read(db, "fast_sessions");
        if (!_infoFastSessions.equals(_existingFastSessions)) {
          return new RoomOpenHelper.ValidationResult(false, "fast_sessions(kr.co.oadan.data.model.FastSession).\n"
                  + " Expected:\n" + _infoFastSessions + "\n"
                  + " Found:\n" + _existingFastSessions);
        }
        final HashMap<String, TableInfo.Column> _columnsWaterLogs = new HashMap<String, TableInfo.Column>(4);
        _columnsWaterLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWaterLogs.put("timestampUtc", new TableInfo.Column("timestampUtc", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWaterLogs.put("amountMl", new TableInfo.Column("amountMl", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWaterLogs.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWaterLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWaterLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoWaterLogs = new TableInfo("water_logs", _columnsWaterLogs, _foreignKeysWaterLogs, _indicesWaterLogs);
        final TableInfo _existingWaterLogs = TableInfo.read(db, "water_logs");
        if (!_infoWaterLogs.equals(_existingWaterLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "water_logs(kr.co.oadan.data.model.WaterLog).\n"
                  + " Expected:\n" + _infoWaterLogs + "\n"
                  + " Found:\n" + _existingWaterLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsConditionLogs = new HashMap<String, TableInfo.Column>(8);
        _columnsConditionLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConditionLogs.put("timestampUtc", new TableInfo.Column("timestampUtc", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConditionLogs.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConditionLogs.put("weightKg", new TableInfo.Column("weightKg", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConditionLogs.put("skeletalMuscleKg", new TableInfo.Column("skeletalMuscleKg", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConditionLogs.put("bodyFatPercent", new TableInfo.Column("bodyFatPercent", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConditionLogs.put("conditionTag", new TableInfo.Column("conditionTag", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConditionLogs.put("memo", new TableInfo.Column("memo", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysConditionLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesConditionLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoConditionLogs = new TableInfo("condition_logs", _columnsConditionLogs, _foreignKeysConditionLogs, _indicesConditionLogs);
        final TableInfo _existingConditionLogs = TableInfo.read(db, "condition_logs");
        if (!_infoConditionLogs.equals(_existingConditionLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "condition_logs(kr.co.oadan.data.model.ConditionLog).\n"
                  + " Expected:\n" + _infoConditionLogs + "\n"
                  + " Found:\n" + _existingConditionLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsMealLogs = new HashMap<String, TableInfo.Column>(15);
        _columnsMealLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealLogs.put("fastSessionId", new TableInfo.Column("fastSessionId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealLogs.put("timestampUtc", new TableInfo.Column("timestampUtc", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealLogs.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealLogs.put("menuName", new TableInfo.Column("menuName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealLogs.put("photoUri", new TableInfo.Column("photoUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealLogs.put("checkVegFirst", new TableInfo.Column("checkVegFirst", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealLogs.put("checkNoProcessed", new TableInfo.Column("checkNoProcessed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealLogs.put("checkSlow20m", new TableInfo.Column("checkSlow20m", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealLogs.put("checkNoSweetDrink", new TableInfo.Column("checkNoSweetDrink", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealLogs.put("checkSeasonal", new TableInfo.Column("checkSeasonal", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealLogs.put("organicScore", new TableInfo.Column("organicScore", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealLogs.put("organicGrade", new TableInfo.Column("organicGrade", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealLogs.put("feedbackMessage", new TableInfo.Column("feedbackMessage", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealLogs.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMealLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMealLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMealLogs = new TableInfo("meal_logs", _columnsMealLogs, _foreignKeysMealLogs, _indicesMealLogs);
        final TableInfo _existingMealLogs = TableInfo.read(db, "meal_logs");
        if (!_infoMealLogs.equals(_existingMealLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "meal_logs(kr.co.oadan.data.model.MealLog).\n"
                  + " Expected:\n" + _infoMealLogs + "\n"
                  + " Found:\n" + _existingMealLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsRecipes = new HashMap<String, TableInfo.Column>(13);
        _columnsRecipes.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipes.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipes.put("subtitle", new TableInfo.Column("subtitle", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipes.put("stage", new TableInfo.Column("stage", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipes.put("emoji", new TableInfo.Column("emoji", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipes.put("prepTimeMinutes", new TableInfo.Column("prepTimeMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipes.put("glycemicFriendly", new TableInfo.Column("glycemicFriendly", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipes.put("tags", new TableInfo.Column("tags", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipes.put("eatOrderGuide", new TableInfo.Column("eatOrderGuide", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipes.put("ingredients", new TableInfo.Column("ingredients", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipes.put("instructions", new TableInfo.Column("instructions", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipes.put("seasonInfo", new TableInfo.Column("seasonInfo", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipes.put("isBookmarked", new TableInfo.Column("isBookmarked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRecipes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRecipes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRecipes = new TableInfo("recipes", _columnsRecipes, _foreignKeysRecipes, _indicesRecipes);
        final TableInfo _existingRecipes = TableInfo.read(db, "recipes");
        if (!_infoRecipes.equals(_existingRecipes)) {
          return new RoomOpenHelper.ValidationResult(false, "recipes(kr.co.oadan.data.model.Recipe).\n"
                  + " Expected:\n" + _infoRecipes + "\n"
                  + " Found:\n" + _existingRecipes);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "e6fec75a703af0858b1b24d386a2b5db", "c44b5319b837117906e4a510ad812f98");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "fast_sessions","water_logs","condition_logs","meal_logs","recipes");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `fast_sessions`");
      _db.execSQL("DELETE FROM `water_logs`");
      _db.execSQL("DELETE FROM `condition_logs`");
      _db.execSQL("DELETE FROM `meal_logs`");
      _db.execSQL("DELETE FROM `recipes`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(FastSessionDao.class, FastSessionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(WaterLogDao.class, WaterLogDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ConditionLogDao.class, ConditionLogDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MealLogDao.class, MealLogDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RecipeDao.class, RecipeDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public FastSessionDao fastSessionDao() {
    if (_fastSessionDao != null) {
      return _fastSessionDao;
    } else {
      synchronized(this) {
        if(_fastSessionDao == null) {
          _fastSessionDao = new FastSessionDao_Impl(this);
        }
        return _fastSessionDao;
      }
    }
  }

  @Override
  public WaterLogDao waterLogDao() {
    if (_waterLogDao != null) {
      return _waterLogDao;
    } else {
      synchronized(this) {
        if(_waterLogDao == null) {
          _waterLogDao = new WaterLogDao_Impl(this);
        }
        return _waterLogDao;
      }
    }
  }

  @Override
  public ConditionLogDao conditionLogDao() {
    if (_conditionLogDao != null) {
      return _conditionLogDao;
    } else {
      synchronized(this) {
        if(_conditionLogDao == null) {
          _conditionLogDao = new ConditionLogDao_Impl(this);
        }
        return _conditionLogDao;
      }
    }
  }

  @Override
  public MealLogDao mealLogDao() {
    if (_mealLogDao != null) {
      return _mealLogDao;
    } else {
      synchronized(this) {
        if(_mealLogDao == null) {
          _mealLogDao = new MealLogDao_Impl(this);
        }
        return _mealLogDao;
      }
    }
  }

  @Override
  public RecipeDao recipeDao() {
    if (_recipeDao != null) {
      return _recipeDao;
    } else {
      synchronized(this) {
        if(_recipeDao == null) {
          _recipeDao = new RecipeDao_Impl(this);
        }
        return _recipeDao;
      }
    }
  }
}
