package kr.co.oadan.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;
import kr.co.oadan.data.model.Recipe;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RecipeDao_Impl implements RecipeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Recipe> __insertionAdapterOfRecipe;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<Recipe> __updateAdapterOfRecipe;

  public RecipeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRecipe = new EntityInsertionAdapter<Recipe>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `recipes` (`id`,`title`,`subtitle`,`stage`,`emoji`,`prepTimeMinutes`,`glycemicFriendly`,`tags`,`eatOrderGuide`,`ingredients`,`instructions`,`seasonInfo`,`isBookmarked`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Recipe entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getSubtitle());
        statement.bindString(4, entity.getStage());
        statement.bindString(5, entity.getEmoji());
        statement.bindLong(6, entity.getPrepTimeMinutes());
        statement.bindString(7, entity.getGlycemicFriendly());
        final String _tmp = __converters.fromStringList(entity.getTags());
        statement.bindString(8, _tmp);
        statement.bindString(9, entity.getEatOrderGuide());
        final String _tmp_1 = __converters.fromStringList(entity.getIngredients());
        statement.bindString(10, _tmp_1);
        final String _tmp_2 = __converters.fromStringList(entity.getInstructions());
        statement.bindString(11, _tmp_2);
        if (entity.getSeasonInfo() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getSeasonInfo());
        }
        final int _tmp_3 = entity.isBookmarked() ? 1 : 0;
        statement.bindLong(13, _tmp_3);
      }
    };
    this.__updateAdapterOfRecipe = new EntityDeletionOrUpdateAdapter<Recipe>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `recipes` SET `id` = ?,`title` = ?,`subtitle` = ?,`stage` = ?,`emoji` = ?,`prepTimeMinutes` = ?,`glycemicFriendly` = ?,`tags` = ?,`eatOrderGuide` = ?,`ingredients` = ?,`instructions` = ?,`seasonInfo` = ?,`isBookmarked` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Recipe entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getSubtitle());
        statement.bindString(4, entity.getStage());
        statement.bindString(5, entity.getEmoji());
        statement.bindLong(6, entity.getPrepTimeMinutes());
        statement.bindString(7, entity.getGlycemicFriendly());
        final String _tmp = __converters.fromStringList(entity.getTags());
        statement.bindString(8, _tmp);
        statement.bindString(9, entity.getEatOrderGuide());
        final String _tmp_1 = __converters.fromStringList(entity.getIngredients());
        statement.bindString(10, _tmp_1);
        final String _tmp_2 = __converters.fromStringList(entity.getInstructions());
        statement.bindString(11, _tmp_2);
        if (entity.getSeasonInfo() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getSeasonInfo());
        }
        final int _tmp_3 = entity.isBookmarked() ? 1 : 0;
        statement.bindLong(13, _tmp_3);
        statement.bindString(14, entity.getId());
      }
    };
  }

  @Override
  public Object insertAll(final List<Recipe> recipes,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRecipe.insert(recipes);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Recipe recipe, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRecipe.handle(recipe);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Recipe>> getAllRecipes() {
    final String _sql = "SELECT * FROM recipes";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"recipes"}, new Callable<List<Recipe>>() {
      @Override
      @NonNull
      public List<Recipe> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfSubtitle = CursorUtil.getColumnIndexOrThrow(_cursor, "subtitle");
          final int _cursorIndexOfStage = CursorUtil.getColumnIndexOrThrow(_cursor, "stage");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfPrepTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "prepTimeMinutes");
          final int _cursorIndexOfGlycemicFriendly = CursorUtil.getColumnIndexOrThrow(_cursor, "glycemicFriendly");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfEatOrderGuide = CursorUtil.getColumnIndexOrThrow(_cursor, "eatOrderGuide");
          final int _cursorIndexOfIngredients = CursorUtil.getColumnIndexOrThrow(_cursor, "ingredients");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfSeasonInfo = CursorUtil.getColumnIndexOrThrow(_cursor, "seasonInfo");
          final int _cursorIndexOfIsBookmarked = CursorUtil.getColumnIndexOrThrow(_cursor, "isBookmarked");
          final List<Recipe> _result = new ArrayList<Recipe>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Recipe _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpSubtitle;
            _tmpSubtitle = _cursor.getString(_cursorIndexOfSubtitle);
            final String _tmpStage;
            _tmpStage = _cursor.getString(_cursorIndexOfStage);
            final String _tmpEmoji;
            _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            final int _tmpPrepTimeMinutes;
            _tmpPrepTimeMinutes = _cursor.getInt(_cursorIndexOfPrepTimeMinutes);
            final String _tmpGlycemicFriendly;
            _tmpGlycemicFriendly = _cursor.getString(_cursorIndexOfGlycemicFriendly);
            final List<String> _tmpTags;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfTags);
            _tmpTags = __converters.toStringList(_tmp);
            final String _tmpEatOrderGuide;
            _tmpEatOrderGuide = _cursor.getString(_cursorIndexOfEatOrderGuide);
            final List<String> _tmpIngredients;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfIngredients);
            _tmpIngredients = __converters.toStringList(_tmp_1);
            final List<String> _tmpInstructions;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfInstructions);
            _tmpInstructions = __converters.toStringList(_tmp_2);
            final String _tmpSeasonInfo;
            if (_cursor.isNull(_cursorIndexOfSeasonInfo)) {
              _tmpSeasonInfo = null;
            } else {
              _tmpSeasonInfo = _cursor.getString(_cursorIndexOfSeasonInfo);
            }
            final boolean _tmpIsBookmarked;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsBookmarked);
            _tmpIsBookmarked = _tmp_3 != 0;
            _item = new Recipe(_tmpId,_tmpTitle,_tmpSubtitle,_tmpStage,_tmpEmoji,_tmpPrepTimeMinutes,_tmpGlycemicFriendly,_tmpTags,_tmpEatOrderGuide,_tmpIngredients,_tmpInstructions,_tmpSeasonInfo,_tmpIsBookmarked);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Recipe>> getRecipesByStage(final String stage) {
    final String _sql = "SELECT * FROM recipes WHERE stage = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, stage);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"recipes"}, new Callable<List<Recipe>>() {
      @Override
      @NonNull
      public List<Recipe> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfSubtitle = CursorUtil.getColumnIndexOrThrow(_cursor, "subtitle");
          final int _cursorIndexOfStage = CursorUtil.getColumnIndexOrThrow(_cursor, "stage");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfPrepTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "prepTimeMinutes");
          final int _cursorIndexOfGlycemicFriendly = CursorUtil.getColumnIndexOrThrow(_cursor, "glycemicFriendly");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfEatOrderGuide = CursorUtil.getColumnIndexOrThrow(_cursor, "eatOrderGuide");
          final int _cursorIndexOfIngredients = CursorUtil.getColumnIndexOrThrow(_cursor, "ingredients");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfSeasonInfo = CursorUtil.getColumnIndexOrThrow(_cursor, "seasonInfo");
          final int _cursorIndexOfIsBookmarked = CursorUtil.getColumnIndexOrThrow(_cursor, "isBookmarked");
          final List<Recipe> _result = new ArrayList<Recipe>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Recipe _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpSubtitle;
            _tmpSubtitle = _cursor.getString(_cursorIndexOfSubtitle);
            final String _tmpStage;
            _tmpStage = _cursor.getString(_cursorIndexOfStage);
            final String _tmpEmoji;
            _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            final int _tmpPrepTimeMinutes;
            _tmpPrepTimeMinutes = _cursor.getInt(_cursorIndexOfPrepTimeMinutes);
            final String _tmpGlycemicFriendly;
            _tmpGlycemicFriendly = _cursor.getString(_cursorIndexOfGlycemicFriendly);
            final List<String> _tmpTags;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfTags);
            _tmpTags = __converters.toStringList(_tmp);
            final String _tmpEatOrderGuide;
            _tmpEatOrderGuide = _cursor.getString(_cursorIndexOfEatOrderGuide);
            final List<String> _tmpIngredients;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfIngredients);
            _tmpIngredients = __converters.toStringList(_tmp_1);
            final List<String> _tmpInstructions;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfInstructions);
            _tmpInstructions = __converters.toStringList(_tmp_2);
            final String _tmpSeasonInfo;
            if (_cursor.isNull(_cursorIndexOfSeasonInfo)) {
              _tmpSeasonInfo = null;
            } else {
              _tmpSeasonInfo = _cursor.getString(_cursorIndexOfSeasonInfo);
            }
            final boolean _tmpIsBookmarked;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsBookmarked);
            _tmpIsBookmarked = _tmp_3 != 0;
            _item = new Recipe(_tmpId,_tmpTitle,_tmpSubtitle,_tmpStage,_tmpEmoji,_tmpPrepTimeMinutes,_tmpGlycemicFriendly,_tmpTags,_tmpEatOrderGuide,_tmpIngredients,_tmpInstructions,_tmpSeasonInfo,_tmpIsBookmarked);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getRecipeById(final String id, final Continuation<? super Recipe> $completion) {
    final String _sql = "SELECT * FROM recipes WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Recipe>() {
      @Override
      @Nullable
      public Recipe call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfSubtitle = CursorUtil.getColumnIndexOrThrow(_cursor, "subtitle");
          final int _cursorIndexOfStage = CursorUtil.getColumnIndexOrThrow(_cursor, "stage");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfPrepTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "prepTimeMinutes");
          final int _cursorIndexOfGlycemicFriendly = CursorUtil.getColumnIndexOrThrow(_cursor, "glycemicFriendly");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfEatOrderGuide = CursorUtil.getColumnIndexOrThrow(_cursor, "eatOrderGuide");
          final int _cursorIndexOfIngredients = CursorUtil.getColumnIndexOrThrow(_cursor, "ingredients");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfSeasonInfo = CursorUtil.getColumnIndexOrThrow(_cursor, "seasonInfo");
          final int _cursorIndexOfIsBookmarked = CursorUtil.getColumnIndexOrThrow(_cursor, "isBookmarked");
          final Recipe _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpSubtitle;
            _tmpSubtitle = _cursor.getString(_cursorIndexOfSubtitle);
            final String _tmpStage;
            _tmpStage = _cursor.getString(_cursorIndexOfStage);
            final String _tmpEmoji;
            _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            final int _tmpPrepTimeMinutes;
            _tmpPrepTimeMinutes = _cursor.getInt(_cursorIndexOfPrepTimeMinutes);
            final String _tmpGlycemicFriendly;
            _tmpGlycemicFriendly = _cursor.getString(_cursorIndexOfGlycemicFriendly);
            final List<String> _tmpTags;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfTags);
            _tmpTags = __converters.toStringList(_tmp);
            final String _tmpEatOrderGuide;
            _tmpEatOrderGuide = _cursor.getString(_cursorIndexOfEatOrderGuide);
            final List<String> _tmpIngredients;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfIngredients);
            _tmpIngredients = __converters.toStringList(_tmp_1);
            final List<String> _tmpInstructions;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfInstructions);
            _tmpInstructions = __converters.toStringList(_tmp_2);
            final String _tmpSeasonInfo;
            if (_cursor.isNull(_cursorIndexOfSeasonInfo)) {
              _tmpSeasonInfo = null;
            } else {
              _tmpSeasonInfo = _cursor.getString(_cursorIndexOfSeasonInfo);
            }
            final boolean _tmpIsBookmarked;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsBookmarked);
            _tmpIsBookmarked = _tmp_3 != 0;
            _result = new Recipe(_tmpId,_tmpTitle,_tmpSubtitle,_tmpStage,_tmpEmoji,_tmpPrepTimeMinutes,_tmpGlycemicFriendly,_tmpTags,_tmpEatOrderGuide,_tmpIngredients,_tmpInstructions,_tmpSeasonInfo,_tmpIsBookmarked);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object count(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM recipes";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
