package kr.co.oadan.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
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
import kr.co.oadan.data.model.MealLog;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MealLogDao_Impl implements MealLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MealLog> __insertionAdapterOfMealLog;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public MealLogDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMealLog = new EntityInsertionAdapter<MealLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `meal_logs` (`id`,`fastSessionId`,`timestampUtc`,`date`,`menuName`,`photoUri`,`checkVegFirst`,`checkNoProcessed`,`checkSlow20m`,`checkNoSweetDrink`,`checkSeasonal`,`organicScore`,`organicGrade`,`feedbackMessage`,`notes`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MealLog entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getFastSessionId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getFastSessionId());
        }
        statement.bindLong(3, entity.getTimestampUtc());
        statement.bindString(4, entity.getDate());
        statement.bindString(5, entity.getMenuName());
        if (entity.getPhotoUri() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPhotoUri());
        }
        final int _tmp = entity.getCheckVegFirst() ? 1 : 0;
        statement.bindLong(7, _tmp);
        final int _tmp_1 = entity.getCheckNoProcessed() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        final int _tmp_2 = entity.getCheckSlow20m() ? 1 : 0;
        statement.bindLong(9, _tmp_2);
        final int _tmp_3 = entity.getCheckNoSweetDrink() ? 1 : 0;
        statement.bindLong(10, _tmp_3);
        final int _tmp_4 = entity.getCheckSeasonal() ? 1 : 0;
        statement.bindLong(11, _tmp_4);
        statement.bindLong(12, entity.getOrganicScore());
        statement.bindString(13, entity.getOrganicGrade());
        statement.bindString(14, entity.getFeedbackMessage());
        if (entity.getNotes() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getNotes());
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM meal_logs";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final MealLog log, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMealLog.insertAndReturnId(log);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MealLog>> getMealLogsForDate(final String date) {
    final String _sql = "SELECT * FROM meal_logs WHERE date = ? ORDER BY timestampUtc DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"meal_logs"}, new Callable<List<MealLog>>() {
      @Override
      @NonNull
      public List<MealLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFastSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "fastSessionId");
          final int _cursorIndexOfTimestampUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampUtc");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfMenuName = CursorUtil.getColumnIndexOrThrow(_cursor, "menuName");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final int _cursorIndexOfCheckVegFirst = CursorUtil.getColumnIndexOrThrow(_cursor, "checkVegFirst");
          final int _cursorIndexOfCheckNoProcessed = CursorUtil.getColumnIndexOrThrow(_cursor, "checkNoProcessed");
          final int _cursorIndexOfCheckSlow20m = CursorUtil.getColumnIndexOrThrow(_cursor, "checkSlow20m");
          final int _cursorIndexOfCheckNoSweetDrink = CursorUtil.getColumnIndexOrThrow(_cursor, "checkNoSweetDrink");
          final int _cursorIndexOfCheckSeasonal = CursorUtil.getColumnIndexOrThrow(_cursor, "checkSeasonal");
          final int _cursorIndexOfOrganicScore = CursorUtil.getColumnIndexOrThrow(_cursor, "organicScore");
          final int _cursorIndexOfOrganicGrade = CursorUtil.getColumnIndexOrThrow(_cursor, "organicGrade");
          final int _cursorIndexOfFeedbackMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "feedbackMessage");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<MealLog> _result = new ArrayList<MealLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MealLog _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpFastSessionId;
            if (_cursor.isNull(_cursorIndexOfFastSessionId)) {
              _tmpFastSessionId = null;
            } else {
              _tmpFastSessionId = _cursor.getLong(_cursorIndexOfFastSessionId);
            }
            final long _tmpTimestampUtc;
            _tmpTimestampUtc = _cursor.getLong(_cursorIndexOfTimestampUtc);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpMenuName;
            _tmpMenuName = _cursor.getString(_cursorIndexOfMenuName);
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            final boolean _tmpCheckVegFirst;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfCheckVegFirst);
            _tmpCheckVegFirst = _tmp != 0;
            final boolean _tmpCheckNoProcessed;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfCheckNoProcessed);
            _tmpCheckNoProcessed = _tmp_1 != 0;
            final boolean _tmpCheckSlow20m;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfCheckSlow20m);
            _tmpCheckSlow20m = _tmp_2 != 0;
            final boolean _tmpCheckNoSweetDrink;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfCheckNoSweetDrink);
            _tmpCheckNoSweetDrink = _tmp_3 != 0;
            final boolean _tmpCheckSeasonal;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfCheckSeasonal);
            _tmpCheckSeasonal = _tmp_4 != 0;
            final int _tmpOrganicScore;
            _tmpOrganicScore = _cursor.getInt(_cursorIndexOfOrganicScore);
            final String _tmpOrganicGrade;
            _tmpOrganicGrade = _cursor.getString(_cursorIndexOfOrganicGrade);
            final String _tmpFeedbackMessage;
            _tmpFeedbackMessage = _cursor.getString(_cursorIndexOfFeedbackMessage);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item = new MealLog(_tmpId,_tmpFastSessionId,_tmpTimestampUtc,_tmpDate,_tmpMenuName,_tmpPhotoUri,_tmpCheckVegFirst,_tmpCheckNoProcessed,_tmpCheckSlow20m,_tmpCheckNoSweetDrink,_tmpCheckSeasonal,_tmpOrganicScore,_tmpOrganicGrade,_tmpFeedbackMessage,_tmpNotes);
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
  public Flow<List<MealLog>> getAllMealLogs() {
    final String _sql = "SELECT * FROM meal_logs ORDER BY timestampUtc DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"meal_logs"}, new Callable<List<MealLog>>() {
      @Override
      @NonNull
      public List<MealLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFastSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "fastSessionId");
          final int _cursorIndexOfTimestampUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampUtc");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfMenuName = CursorUtil.getColumnIndexOrThrow(_cursor, "menuName");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final int _cursorIndexOfCheckVegFirst = CursorUtil.getColumnIndexOrThrow(_cursor, "checkVegFirst");
          final int _cursorIndexOfCheckNoProcessed = CursorUtil.getColumnIndexOrThrow(_cursor, "checkNoProcessed");
          final int _cursorIndexOfCheckSlow20m = CursorUtil.getColumnIndexOrThrow(_cursor, "checkSlow20m");
          final int _cursorIndexOfCheckNoSweetDrink = CursorUtil.getColumnIndexOrThrow(_cursor, "checkNoSweetDrink");
          final int _cursorIndexOfCheckSeasonal = CursorUtil.getColumnIndexOrThrow(_cursor, "checkSeasonal");
          final int _cursorIndexOfOrganicScore = CursorUtil.getColumnIndexOrThrow(_cursor, "organicScore");
          final int _cursorIndexOfOrganicGrade = CursorUtil.getColumnIndexOrThrow(_cursor, "organicGrade");
          final int _cursorIndexOfFeedbackMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "feedbackMessage");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<MealLog> _result = new ArrayList<MealLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MealLog _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpFastSessionId;
            if (_cursor.isNull(_cursorIndexOfFastSessionId)) {
              _tmpFastSessionId = null;
            } else {
              _tmpFastSessionId = _cursor.getLong(_cursorIndexOfFastSessionId);
            }
            final long _tmpTimestampUtc;
            _tmpTimestampUtc = _cursor.getLong(_cursorIndexOfTimestampUtc);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpMenuName;
            _tmpMenuName = _cursor.getString(_cursorIndexOfMenuName);
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            final boolean _tmpCheckVegFirst;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfCheckVegFirst);
            _tmpCheckVegFirst = _tmp != 0;
            final boolean _tmpCheckNoProcessed;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfCheckNoProcessed);
            _tmpCheckNoProcessed = _tmp_1 != 0;
            final boolean _tmpCheckSlow20m;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfCheckSlow20m);
            _tmpCheckSlow20m = _tmp_2 != 0;
            final boolean _tmpCheckNoSweetDrink;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfCheckNoSweetDrink);
            _tmpCheckNoSweetDrink = _tmp_3 != 0;
            final boolean _tmpCheckSeasonal;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfCheckSeasonal);
            _tmpCheckSeasonal = _tmp_4 != 0;
            final int _tmpOrganicScore;
            _tmpOrganicScore = _cursor.getInt(_cursorIndexOfOrganicScore);
            final String _tmpOrganicGrade;
            _tmpOrganicGrade = _cursor.getString(_cursorIndexOfOrganicGrade);
            final String _tmpFeedbackMessage;
            _tmpFeedbackMessage = _cursor.getString(_cursorIndexOfFeedbackMessage);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item = new MealLog(_tmpId,_tmpFastSessionId,_tmpTimestampUtc,_tmpDate,_tmpMenuName,_tmpPhotoUri,_tmpCheckVegFirst,_tmpCheckNoProcessed,_tmpCheckSlow20m,_tmpCheckNoSweetDrink,_tmpCheckSeasonal,_tmpOrganicScore,_tmpOrganicGrade,_tmpFeedbackMessage,_tmpNotes);
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
  public Object getAllMealLogsOnce(final Continuation<? super List<MealLog>> $completion) {
    final String _sql = "SELECT * FROM meal_logs ORDER BY timestampUtc DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MealLog>>() {
      @Override
      @NonNull
      public List<MealLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFastSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "fastSessionId");
          final int _cursorIndexOfTimestampUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampUtc");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfMenuName = CursorUtil.getColumnIndexOrThrow(_cursor, "menuName");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final int _cursorIndexOfCheckVegFirst = CursorUtil.getColumnIndexOrThrow(_cursor, "checkVegFirst");
          final int _cursorIndexOfCheckNoProcessed = CursorUtil.getColumnIndexOrThrow(_cursor, "checkNoProcessed");
          final int _cursorIndexOfCheckSlow20m = CursorUtil.getColumnIndexOrThrow(_cursor, "checkSlow20m");
          final int _cursorIndexOfCheckNoSweetDrink = CursorUtil.getColumnIndexOrThrow(_cursor, "checkNoSweetDrink");
          final int _cursorIndexOfCheckSeasonal = CursorUtil.getColumnIndexOrThrow(_cursor, "checkSeasonal");
          final int _cursorIndexOfOrganicScore = CursorUtil.getColumnIndexOrThrow(_cursor, "organicScore");
          final int _cursorIndexOfOrganicGrade = CursorUtil.getColumnIndexOrThrow(_cursor, "organicGrade");
          final int _cursorIndexOfFeedbackMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "feedbackMessage");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<MealLog> _result = new ArrayList<MealLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MealLog _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpFastSessionId;
            if (_cursor.isNull(_cursorIndexOfFastSessionId)) {
              _tmpFastSessionId = null;
            } else {
              _tmpFastSessionId = _cursor.getLong(_cursorIndexOfFastSessionId);
            }
            final long _tmpTimestampUtc;
            _tmpTimestampUtc = _cursor.getLong(_cursorIndexOfTimestampUtc);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpMenuName;
            _tmpMenuName = _cursor.getString(_cursorIndexOfMenuName);
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            final boolean _tmpCheckVegFirst;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfCheckVegFirst);
            _tmpCheckVegFirst = _tmp != 0;
            final boolean _tmpCheckNoProcessed;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfCheckNoProcessed);
            _tmpCheckNoProcessed = _tmp_1 != 0;
            final boolean _tmpCheckSlow20m;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfCheckSlow20m);
            _tmpCheckSlow20m = _tmp_2 != 0;
            final boolean _tmpCheckNoSweetDrink;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfCheckNoSweetDrink);
            _tmpCheckNoSweetDrink = _tmp_3 != 0;
            final boolean _tmpCheckSeasonal;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfCheckSeasonal);
            _tmpCheckSeasonal = _tmp_4 != 0;
            final int _tmpOrganicScore;
            _tmpOrganicScore = _cursor.getInt(_cursorIndexOfOrganicScore);
            final String _tmpOrganicGrade;
            _tmpOrganicGrade = _cursor.getString(_cursorIndexOfOrganicGrade);
            final String _tmpFeedbackMessage;
            _tmpFeedbackMessage = _cursor.getString(_cursorIndexOfFeedbackMessage);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item = new MealLog(_tmpId,_tmpFastSessionId,_tmpTimestampUtc,_tmpDate,_tmpMenuName,_tmpPhotoUri,_tmpCheckVegFirst,_tmpCheckNoProcessed,_tmpCheckSlow20m,_tmpCheckNoSweetDrink,_tmpCheckSeasonal,_tmpOrganicScore,_tmpOrganicGrade,_tmpFeedbackMessage,_tmpNotes);
            _result.add(_item);
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
  public Object getMealLogById(final long id, final Continuation<? super MealLog> $completion) {
    final String _sql = "SELECT * FROM meal_logs WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MealLog>() {
      @Override
      @Nullable
      public MealLog call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFastSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "fastSessionId");
          final int _cursorIndexOfTimestampUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampUtc");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfMenuName = CursorUtil.getColumnIndexOrThrow(_cursor, "menuName");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final int _cursorIndexOfCheckVegFirst = CursorUtil.getColumnIndexOrThrow(_cursor, "checkVegFirst");
          final int _cursorIndexOfCheckNoProcessed = CursorUtil.getColumnIndexOrThrow(_cursor, "checkNoProcessed");
          final int _cursorIndexOfCheckSlow20m = CursorUtil.getColumnIndexOrThrow(_cursor, "checkSlow20m");
          final int _cursorIndexOfCheckNoSweetDrink = CursorUtil.getColumnIndexOrThrow(_cursor, "checkNoSweetDrink");
          final int _cursorIndexOfCheckSeasonal = CursorUtil.getColumnIndexOrThrow(_cursor, "checkSeasonal");
          final int _cursorIndexOfOrganicScore = CursorUtil.getColumnIndexOrThrow(_cursor, "organicScore");
          final int _cursorIndexOfOrganicGrade = CursorUtil.getColumnIndexOrThrow(_cursor, "organicGrade");
          final int _cursorIndexOfFeedbackMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "feedbackMessage");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final MealLog _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpFastSessionId;
            if (_cursor.isNull(_cursorIndexOfFastSessionId)) {
              _tmpFastSessionId = null;
            } else {
              _tmpFastSessionId = _cursor.getLong(_cursorIndexOfFastSessionId);
            }
            final long _tmpTimestampUtc;
            _tmpTimestampUtc = _cursor.getLong(_cursorIndexOfTimestampUtc);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpMenuName;
            _tmpMenuName = _cursor.getString(_cursorIndexOfMenuName);
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            final boolean _tmpCheckVegFirst;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfCheckVegFirst);
            _tmpCheckVegFirst = _tmp != 0;
            final boolean _tmpCheckNoProcessed;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfCheckNoProcessed);
            _tmpCheckNoProcessed = _tmp_1 != 0;
            final boolean _tmpCheckSlow20m;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfCheckSlow20m);
            _tmpCheckSlow20m = _tmp_2 != 0;
            final boolean _tmpCheckNoSweetDrink;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfCheckNoSweetDrink);
            _tmpCheckNoSweetDrink = _tmp_3 != 0;
            final boolean _tmpCheckSeasonal;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfCheckSeasonal);
            _tmpCheckSeasonal = _tmp_4 != 0;
            final int _tmpOrganicScore;
            _tmpOrganicScore = _cursor.getInt(_cursorIndexOfOrganicScore);
            final String _tmpOrganicGrade;
            _tmpOrganicGrade = _cursor.getString(_cursorIndexOfOrganicGrade);
            final String _tmpFeedbackMessage;
            _tmpFeedbackMessage = _cursor.getString(_cursorIndexOfFeedbackMessage);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _result = new MealLog(_tmpId,_tmpFastSessionId,_tmpTimestampUtc,_tmpDate,_tmpMenuName,_tmpPhotoUri,_tmpCheckVegFirst,_tmpCheckNoProcessed,_tmpCheckSlow20m,_tmpCheckNoSweetDrink,_tmpCheckSeasonal,_tmpOrganicScore,_tmpOrganicGrade,_tmpFeedbackMessage,_tmpNotes);
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
  public Object getMealLogBySessionId(final long sessionId,
      final Continuation<? super MealLog> $completion) {
    final String _sql = "SELECT * FROM meal_logs WHERE fastSessionId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sessionId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MealLog>() {
      @Override
      @Nullable
      public MealLog call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFastSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "fastSessionId");
          final int _cursorIndexOfTimestampUtc = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampUtc");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfMenuName = CursorUtil.getColumnIndexOrThrow(_cursor, "menuName");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final int _cursorIndexOfCheckVegFirst = CursorUtil.getColumnIndexOrThrow(_cursor, "checkVegFirst");
          final int _cursorIndexOfCheckNoProcessed = CursorUtil.getColumnIndexOrThrow(_cursor, "checkNoProcessed");
          final int _cursorIndexOfCheckSlow20m = CursorUtil.getColumnIndexOrThrow(_cursor, "checkSlow20m");
          final int _cursorIndexOfCheckNoSweetDrink = CursorUtil.getColumnIndexOrThrow(_cursor, "checkNoSweetDrink");
          final int _cursorIndexOfCheckSeasonal = CursorUtil.getColumnIndexOrThrow(_cursor, "checkSeasonal");
          final int _cursorIndexOfOrganicScore = CursorUtil.getColumnIndexOrThrow(_cursor, "organicScore");
          final int _cursorIndexOfOrganicGrade = CursorUtil.getColumnIndexOrThrow(_cursor, "organicGrade");
          final int _cursorIndexOfFeedbackMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "feedbackMessage");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final MealLog _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpFastSessionId;
            if (_cursor.isNull(_cursorIndexOfFastSessionId)) {
              _tmpFastSessionId = null;
            } else {
              _tmpFastSessionId = _cursor.getLong(_cursorIndexOfFastSessionId);
            }
            final long _tmpTimestampUtc;
            _tmpTimestampUtc = _cursor.getLong(_cursorIndexOfTimestampUtc);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpMenuName;
            _tmpMenuName = _cursor.getString(_cursorIndexOfMenuName);
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            final boolean _tmpCheckVegFirst;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfCheckVegFirst);
            _tmpCheckVegFirst = _tmp != 0;
            final boolean _tmpCheckNoProcessed;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfCheckNoProcessed);
            _tmpCheckNoProcessed = _tmp_1 != 0;
            final boolean _tmpCheckSlow20m;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfCheckSlow20m);
            _tmpCheckSlow20m = _tmp_2 != 0;
            final boolean _tmpCheckNoSweetDrink;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfCheckNoSweetDrink);
            _tmpCheckNoSweetDrink = _tmp_3 != 0;
            final boolean _tmpCheckSeasonal;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfCheckSeasonal);
            _tmpCheckSeasonal = _tmp_4 != 0;
            final int _tmpOrganicScore;
            _tmpOrganicScore = _cursor.getInt(_cursorIndexOfOrganicScore);
            final String _tmpOrganicGrade;
            _tmpOrganicGrade = _cursor.getString(_cursorIndexOfOrganicGrade);
            final String _tmpFeedbackMessage;
            _tmpFeedbackMessage = _cursor.getString(_cursorIndexOfFeedbackMessage);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _result = new MealLog(_tmpId,_tmpFastSessionId,_tmpTimestampUtc,_tmpDate,_tmpMenuName,_tmpPhotoUri,_tmpCheckVegFirst,_tmpCheckNoProcessed,_tmpCheckSlow20m,_tmpCheckNoSweetDrink,_tmpCheckSeasonal,_tmpOrganicScore,_tmpOrganicGrade,_tmpFeedbackMessage,_tmpNotes);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
