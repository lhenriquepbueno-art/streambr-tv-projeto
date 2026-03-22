package com.streambr.tv.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
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

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ChannelDao_Impl implements ChannelDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ChannelEntity> __insertionAdapterOfChannelEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public ChannelDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfChannelEntity = new EntityInsertionAdapter<ChannelEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `channels` (`id`,`name`,`logo`,`groupName`,`quality`,`tvgId`,`streamUrl`,`hlsUrl`,`isAdult`,`tvArchive`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ChannelEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getLogo() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getLogo());
        }
        if (entity.getGroupName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getGroupName());
        }
        if (entity.getQuality() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getQuality());
        }
        if (entity.getTvgId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getTvgId());
        }
        if (entity.getStreamUrl() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getStreamUrl());
        }
        if (entity.getHlsUrl() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getHlsUrl());
        }
        final int _tmp = entity.isAdult() ? 1 : 0;
        statement.bindLong(9, _tmp);
        final int _tmp_1 = entity.getTvArchive() ? 1 : 0;
        statement.bindLong(10, _tmp_1);
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM channels";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<ChannelEntity> channels,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfChannelEntity.insert(channels);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object refreshChannels(final List<ChannelEntity> channels,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> ChannelDao.DefaultImpls.refreshChannels(ChannelDao_Impl.this, channels, __cont), $completion);
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
  public Object getAll(final Continuation<? super List<ChannelEntity>> $completion) {
    final String _sql = "SELECT * FROM channels";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ChannelEntity>>() {
      @Override
      @NonNull
      public List<ChannelEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfLogo = CursorUtil.getColumnIndexOrThrow(_cursor, "logo");
          final int _cursorIndexOfGroupName = CursorUtil.getColumnIndexOrThrow(_cursor, "groupName");
          final int _cursorIndexOfQuality = CursorUtil.getColumnIndexOrThrow(_cursor, "quality");
          final int _cursorIndexOfTvgId = CursorUtil.getColumnIndexOrThrow(_cursor, "tvgId");
          final int _cursorIndexOfStreamUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "streamUrl");
          final int _cursorIndexOfHlsUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "hlsUrl");
          final int _cursorIndexOfIsAdult = CursorUtil.getColumnIndexOrThrow(_cursor, "isAdult");
          final int _cursorIndexOfTvArchive = CursorUtil.getColumnIndexOrThrow(_cursor, "tvArchive");
          final List<ChannelEntity> _result = new ArrayList<ChannelEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChannelEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpLogo;
            if (_cursor.isNull(_cursorIndexOfLogo)) {
              _tmpLogo = null;
            } else {
              _tmpLogo = _cursor.getString(_cursorIndexOfLogo);
            }
            final String _tmpGroupName;
            if (_cursor.isNull(_cursorIndexOfGroupName)) {
              _tmpGroupName = null;
            } else {
              _tmpGroupName = _cursor.getString(_cursorIndexOfGroupName);
            }
            final String _tmpQuality;
            if (_cursor.isNull(_cursorIndexOfQuality)) {
              _tmpQuality = null;
            } else {
              _tmpQuality = _cursor.getString(_cursorIndexOfQuality);
            }
            final String _tmpTvgId;
            if (_cursor.isNull(_cursorIndexOfTvgId)) {
              _tmpTvgId = null;
            } else {
              _tmpTvgId = _cursor.getString(_cursorIndexOfTvgId);
            }
            final String _tmpStreamUrl;
            if (_cursor.isNull(_cursorIndexOfStreamUrl)) {
              _tmpStreamUrl = null;
            } else {
              _tmpStreamUrl = _cursor.getString(_cursorIndexOfStreamUrl);
            }
            final String _tmpHlsUrl;
            if (_cursor.isNull(_cursorIndexOfHlsUrl)) {
              _tmpHlsUrl = null;
            } else {
              _tmpHlsUrl = _cursor.getString(_cursorIndexOfHlsUrl);
            }
            final boolean _tmpIsAdult;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsAdult);
            _tmpIsAdult = _tmp != 0;
            final boolean _tmpTvArchive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfTvArchive);
            _tmpTvArchive = _tmp_1 != 0;
            _item = new ChannelEntity(_tmpId,_tmpName,_tmpLogo,_tmpGroupName,_tmpQuality,_tmpTvgId,_tmpStreamUrl,_tmpHlsUrl,_tmpIsAdult,_tmpTvArchive);
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
  public Object getRegularChannels(final Continuation<? super List<ChannelEntity>> $completion) {
    final String _sql = "SELECT * FROM channels WHERE isAdult = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ChannelEntity>>() {
      @Override
      @NonNull
      public List<ChannelEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfLogo = CursorUtil.getColumnIndexOrThrow(_cursor, "logo");
          final int _cursorIndexOfGroupName = CursorUtil.getColumnIndexOrThrow(_cursor, "groupName");
          final int _cursorIndexOfQuality = CursorUtil.getColumnIndexOrThrow(_cursor, "quality");
          final int _cursorIndexOfTvgId = CursorUtil.getColumnIndexOrThrow(_cursor, "tvgId");
          final int _cursorIndexOfStreamUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "streamUrl");
          final int _cursorIndexOfHlsUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "hlsUrl");
          final int _cursorIndexOfIsAdult = CursorUtil.getColumnIndexOrThrow(_cursor, "isAdult");
          final int _cursorIndexOfTvArchive = CursorUtil.getColumnIndexOrThrow(_cursor, "tvArchive");
          final List<ChannelEntity> _result = new ArrayList<ChannelEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChannelEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpLogo;
            if (_cursor.isNull(_cursorIndexOfLogo)) {
              _tmpLogo = null;
            } else {
              _tmpLogo = _cursor.getString(_cursorIndexOfLogo);
            }
            final String _tmpGroupName;
            if (_cursor.isNull(_cursorIndexOfGroupName)) {
              _tmpGroupName = null;
            } else {
              _tmpGroupName = _cursor.getString(_cursorIndexOfGroupName);
            }
            final String _tmpQuality;
            if (_cursor.isNull(_cursorIndexOfQuality)) {
              _tmpQuality = null;
            } else {
              _tmpQuality = _cursor.getString(_cursorIndexOfQuality);
            }
            final String _tmpTvgId;
            if (_cursor.isNull(_cursorIndexOfTvgId)) {
              _tmpTvgId = null;
            } else {
              _tmpTvgId = _cursor.getString(_cursorIndexOfTvgId);
            }
            final String _tmpStreamUrl;
            if (_cursor.isNull(_cursorIndexOfStreamUrl)) {
              _tmpStreamUrl = null;
            } else {
              _tmpStreamUrl = _cursor.getString(_cursorIndexOfStreamUrl);
            }
            final String _tmpHlsUrl;
            if (_cursor.isNull(_cursorIndexOfHlsUrl)) {
              _tmpHlsUrl = null;
            } else {
              _tmpHlsUrl = _cursor.getString(_cursorIndexOfHlsUrl);
            }
            final boolean _tmpIsAdult;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsAdult);
            _tmpIsAdult = _tmp != 0;
            final boolean _tmpTvArchive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfTvArchive);
            _tmpTvArchive = _tmp_1 != 0;
            _item = new ChannelEntity(_tmpId,_tmpName,_tmpLogo,_tmpGroupName,_tmpQuality,_tmpTvgId,_tmpStreamUrl,_tmpHlsUrl,_tmpIsAdult,_tmpTvArchive);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
