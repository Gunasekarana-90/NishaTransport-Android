package com.nishatransport.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.nishatransport.data.local.entity.Load;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
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

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class LoadDao_Impl implements LoadDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Load> __insertionAdapterOfLoad;

  private final EntityDeletionOrUpdateAdapter<Load> __deletionAdapterOfLoad;

  private final EntityDeletionOrUpdateAdapter<Load> __updateAdapterOfLoad;

  private final SharedSQLiteStatement __preparedStmtOfDeleteLoadById;

  public LoadDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLoad = new EntityInsertionAdapter<Load>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `loads` (`id`,`date`,`fromLocation`,`toLocation`,`vehicleNumber`,`notes`,`loadPrice`,`loadingCharge`,`dieselCost`,`policeExpense`,`tollFee`,`driverCharge`,`unloadingCharge`,`otherExpense`,`totalExpense`,`profit`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Load entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDate());
        statement.bindString(3, entity.getFromLocation());
        statement.bindString(4, entity.getToLocation());
        statement.bindString(5, entity.getVehicleNumber());
        statement.bindString(6, entity.getNotes());
        statement.bindDouble(7, entity.getLoadPrice());
        statement.bindDouble(8, entity.getLoadingCharge());
        statement.bindDouble(9, entity.getDieselCost());
        statement.bindDouble(10, entity.getPoliceExpense());
        statement.bindDouble(11, entity.getTollFee());
        statement.bindDouble(12, entity.getDriverCharge());
        statement.bindDouble(13, entity.getUnloadingCharge());
        statement.bindDouble(14, entity.getOtherExpense());
        statement.bindDouble(15, entity.getTotalExpense());
        statement.bindDouble(16, entity.getProfit());
        statement.bindLong(17, entity.getCreatedAt());
        statement.bindLong(18, entity.getUpdatedAt());
      }
    };
    this.__deletionAdapterOfLoad = new EntityDeletionOrUpdateAdapter<Load>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `loads` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Load entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfLoad = new EntityDeletionOrUpdateAdapter<Load>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `loads` SET `id` = ?,`date` = ?,`fromLocation` = ?,`toLocation` = ?,`vehicleNumber` = ?,`notes` = ?,`loadPrice` = ?,`loadingCharge` = ?,`dieselCost` = ?,`policeExpense` = ?,`tollFee` = ?,`driverCharge` = ?,`unloadingCharge` = ?,`otherExpense` = ?,`totalExpense` = ?,`profit` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Load entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDate());
        statement.bindString(3, entity.getFromLocation());
        statement.bindString(4, entity.getToLocation());
        statement.bindString(5, entity.getVehicleNumber());
        statement.bindString(6, entity.getNotes());
        statement.bindDouble(7, entity.getLoadPrice());
        statement.bindDouble(8, entity.getLoadingCharge());
        statement.bindDouble(9, entity.getDieselCost());
        statement.bindDouble(10, entity.getPoliceExpense());
        statement.bindDouble(11, entity.getTollFee());
        statement.bindDouble(12, entity.getDriverCharge());
        statement.bindDouble(13, entity.getUnloadingCharge());
        statement.bindDouble(14, entity.getOtherExpense());
        statement.bindDouble(15, entity.getTotalExpense());
        statement.bindDouble(16, entity.getProfit());
        statement.bindLong(17, entity.getCreatedAt());
        statement.bindLong(18, entity.getUpdatedAt());
        statement.bindLong(19, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteLoadById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM loads WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertLoad(final Load load, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfLoad.insertAndReturnId(load);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteLoad(final Load load, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfLoad.handle(load);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLoad(final Load load, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfLoad.handle(load);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteLoadById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteLoadById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
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
          __preparedStmtOfDeleteLoadById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Load>> getAllLoads() {
    final String _sql = "SELECT * FROM loads ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"loads"}, new Callable<List<Load>>() {
      @Override
      @NonNull
      public List<Load> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfFromLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "fromLocation");
          final int _cursorIndexOfToLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "toLocation");
          final int _cursorIndexOfVehicleNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "vehicleNumber");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfLoadPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "loadPrice");
          final int _cursorIndexOfLoadingCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "loadingCharge");
          final int _cursorIndexOfDieselCost = CursorUtil.getColumnIndexOrThrow(_cursor, "dieselCost");
          final int _cursorIndexOfPoliceExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "policeExpense");
          final int _cursorIndexOfTollFee = CursorUtil.getColumnIndexOrThrow(_cursor, "tollFee");
          final int _cursorIndexOfDriverCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "driverCharge");
          final int _cursorIndexOfUnloadingCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "unloadingCharge");
          final int _cursorIndexOfOtherExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "otherExpense");
          final int _cursorIndexOfTotalExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "totalExpense");
          final int _cursorIndexOfProfit = CursorUtil.getColumnIndexOrThrow(_cursor, "profit");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<Load> _result = new ArrayList<Load>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Load _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final String _tmpFromLocation;
            _tmpFromLocation = _cursor.getString(_cursorIndexOfFromLocation);
            final String _tmpToLocation;
            _tmpToLocation = _cursor.getString(_cursorIndexOfToLocation);
            final String _tmpVehicleNumber;
            _tmpVehicleNumber = _cursor.getString(_cursorIndexOfVehicleNumber);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final double _tmpLoadPrice;
            _tmpLoadPrice = _cursor.getDouble(_cursorIndexOfLoadPrice);
            final double _tmpLoadingCharge;
            _tmpLoadingCharge = _cursor.getDouble(_cursorIndexOfLoadingCharge);
            final double _tmpDieselCost;
            _tmpDieselCost = _cursor.getDouble(_cursorIndexOfDieselCost);
            final double _tmpPoliceExpense;
            _tmpPoliceExpense = _cursor.getDouble(_cursorIndexOfPoliceExpense);
            final double _tmpTollFee;
            _tmpTollFee = _cursor.getDouble(_cursorIndexOfTollFee);
            final double _tmpDriverCharge;
            _tmpDriverCharge = _cursor.getDouble(_cursorIndexOfDriverCharge);
            final double _tmpUnloadingCharge;
            _tmpUnloadingCharge = _cursor.getDouble(_cursorIndexOfUnloadingCharge);
            final double _tmpOtherExpense;
            _tmpOtherExpense = _cursor.getDouble(_cursorIndexOfOtherExpense);
            final double _tmpTotalExpense;
            _tmpTotalExpense = _cursor.getDouble(_cursorIndexOfTotalExpense);
            final double _tmpProfit;
            _tmpProfit = _cursor.getDouble(_cursorIndexOfProfit);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new Load(_tmpId,_tmpDate,_tmpFromLocation,_tmpToLocation,_tmpVehicleNumber,_tmpNotes,_tmpLoadPrice,_tmpLoadingCharge,_tmpDieselCost,_tmpPoliceExpense,_tmpTollFee,_tmpDriverCharge,_tmpUnloadingCharge,_tmpOtherExpense,_tmpTotalExpense,_tmpProfit,_tmpCreatedAt,_tmpUpdatedAt);
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
  public LiveData<List<Load>> getRecentLoads() {
    final String _sql = "SELECT * FROM loads ORDER BY date DESC LIMIT 5";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"loads"}, false, new Callable<List<Load>>() {
      @Override
      @Nullable
      public List<Load> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfFromLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "fromLocation");
          final int _cursorIndexOfToLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "toLocation");
          final int _cursorIndexOfVehicleNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "vehicleNumber");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfLoadPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "loadPrice");
          final int _cursorIndexOfLoadingCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "loadingCharge");
          final int _cursorIndexOfDieselCost = CursorUtil.getColumnIndexOrThrow(_cursor, "dieselCost");
          final int _cursorIndexOfPoliceExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "policeExpense");
          final int _cursorIndexOfTollFee = CursorUtil.getColumnIndexOrThrow(_cursor, "tollFee");
          final int _cursorIndexOfDriverCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "driverCharge");
          final int _cursorIndexOfUnloadingCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "unloadingCharge");
          final int _cursorIndexOfOtherExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "otherExpense");
          final int _cursorIndexOfTotalExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "totalExpense");
          final int _cursorIndexOfProfit = CursorUtil.getColumnIndexOrThrow(_cursor, "profit");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<Load> _result = new ArrayList<Load>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Load _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final String _tmpFromLocation;
            _tmpFromLocation = _cursor.getString(_cursorIndexOfFromLocation);
            final String _tmpToLocation;
            _tmpToLocation = _cursor.getString(_cursorIndexOfToLocation);
            final String _tmpVehicleNumber;
            _tmpVehicleNumber = _cursor.getString(_cursorIndexOfVehicleNumber);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final double _tmpLoadPrice;
            _tmpLoadPrice = _cursor.getDouble(_cursorIndexOfLoadPrice);
            final double _tmpLoadingCharge;
            _tmpLoadingCharge = _cursor.getDouble(_cursorIndexOfLoadingCharge);
            final double _tmpDieselCost;
            _tmpDieselCost = _cursor.getDouble(_cursorIndexOfDieselCost);
            final double _tmpPoliceExpense;
            _tmpPoliceExpense = _cursor.getDouble(_cursorIndexOfPoliceExpense);
            final double _tmpTollFee;
            _tmpTollFee = _cursor.getDouble(_cursorIndexOfTollFee);
            final double _tmpDriverCharge;
            _tmpDriverCharge = _cursor.getDouble(_cursorIndexOfDriverCharge);
            final double _tmpUnloadingCharge;
            _tmpUnloadingCharge = _cursor.getDouble(_cursorIndexOfUnloadingCharge);
            final double _tmpOtherExpense;
            _tmpOtherExpense = _cursor.getDouble(_cursorIndexOfOtherExpense);
            final double _tmpTotalExpense;
            _tmpTotalExpense = _cursor.getDouble(_cursorIndexOfTotalExpense);
            final double _tmpProfit;
            _tmpProfit = _cursor.getDouble(_cursorIndexOfProfit);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new Load(_tmpId,_tmpDate,_tmpFromLocation,_tmpToLocation,_tmpVehicleNumber,_tmpNotes,_tmpLoadPrice,_tmpLoadingCharge,_tmpDieselCost,_tmpPoliceExpense,_tmpTollFee,_tmpDriverCharge,_tmpUnloadingCharge,_tmpOtherExpense,_tmpTotalExpense,_tmpProfit,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getLoadById(final long id, final Continuation<? super Load> $completion) {
    final String _sql = "SELECT * FROM loads WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Load>() {
      @Override
      @Nullable
      public Load call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfFromLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "fromLocation");
          final int _cursorIndexOfToLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "toLocation");
          final int _cursorIndexOfVehicleNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "vehicleNumber");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfLoadPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "loadPrice");
          final int _cursorIndexOfLoadingCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "loadingCharge");
          final int _cursorIndexOfDieselCost = CursorUtil.getColumnIndexOrThrow(_cursor, "dieselCost");
          final int _cursorIndexOfPoliceExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "policeExpense");
          final int _cursorIndexOfTollFee = CursorUtil.getColumnIndexOrThrow(_cursor, "tollFee");
          final int _cursorIndexOfDriverCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "driverCharge");
          final int _cursorIndexOfUnloadingCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "unloadingCharge");
          final int _cursorIndexOfOtherExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "otherExpense");
          final int _cursorIndexOfTotalExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "totalExpense");
          final int _cursorIndexOfProfit = CursorUtil.getColumnIndexOrThrow(_cursor, "profit");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final Load _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final String _tmpFromLocation;
            _tmpFromLocation = _cursor.getString(_cursorIndexOfFromLocation);
            final String _tmpToLocation;
            _tmpToLocation = _cursor.getString(_cursorIndexOfToLocation);
            final String _tmpVehicleNumber;
            _tmpVehicleNumber = _cursor.getString(_cursorIndexOfVehicleNumber);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final double _tmpLoadPrice;
            _tmpLoadPrice = _cursor.getDouble(_cursorIndexOfLoadPrice);
            final double _tmpLoadingCharge;
            _tmpLoadingCharge = _cursor.getDouble(_cursorIndexOfLoadingCharge);
            final double _tmpDieselCost;
            _tmpDieselCost = _cursor.getDouble(_cursorIndexOfDieselCost);
            final double _tmpPoliceExpense;
            _tmpPoliceExpense = _cursor.getDouble(_cursorIndexOfPoliceExpense);
            final double _tmpTollFee;
            _tmpTollFee = _cursor.getDouble(_cursorIndexOfTollFee);
            final double _tmpDriverCharge;
            _tmpDriverCharge = _cursor.getDouble(_cursorIndexOfDriverCharge);
            final double _tmpUnloadingCharge;
            _tmpUnloadingCharge = _cursor.getDouble(_cursorIndexOfUnloadingCharge);
            final double _tmpOtherExpense;
            _tmpOtherExpense = _cursor.getDouble(_cursorIndexOfOtherExpense);
            final double _tmpTotalExpense;
            _tmpTotalExpense = _cursor.getDouble(_cursorIndexOfTotalExpense);
            final double _tmpProfit;
            _tmpProfit = _cursor.getDouble(_cursorIndexOfProfit);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new Load(_tmpId,_tmpDate,_tmpFromLocation,_tmpToLocation,_tmpVehicleNumber,_tmpNotes,_tmpLoadPrice,_tmpLoadingCharge,_tmpDieselCost,_tmpPoliceExpense,_tmpTollFee,_tmpDriverCharge,_tmpUnloadingCharge,_tmpOtherExpense,_tmpTotalExpense,_tmpProfit,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<Load>> searchLoads(final String query) {
    final String _sql = "\n"
            + "        SELECT * FROM loads \n"
            + "        WHERE (fromLocation LIKE '%' || ? || '%' \n"
            + "            OR toLocation LIKE '%' || ? || '%'\n"
            + "            OR vehicleNumber LIKE '%' || ? || '%')\n"
            + "        ORDER BY date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    _argIndex = 2;
    _statement.bindString(_argIndex, query);
    _argIndex = 3;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"loads"}, new Callable<List<Load>>() {
      @Override
      @NonNull
      public List<Load> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfFromLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "fromLocation");
          final int _cursorIndexOfToLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "toLocation");
          final int _cursorIndexOfVehicleNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "vehicleNumber");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfLoadPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "loadPrice");
          final int _cursorIndexOfLoadingCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "loadingCharge");
          final int _cursorIndexOfDieselCost = CursorUtil.getColumnIndexOrThrow(_cursor, "dieselCost");
          final int _cursorIndexOfPoliceExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "policeExpense");
          final int _cursorIndexOfTollFee = CursorUtil.getColumnIndexOrThrow(_cursor, "tollFee");
          final int _cursorIndexOfDriverCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "driverCharge");
          final int _cursorIndexOfUnloadingCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "unloadingCharge");
          final int _cursorIndexOfOtherExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "otherExpense");
          final int _cursorIndexOfTotalExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "totalExpense");
          final int _cursorIndexOfProfit = CursorUtil.getColumnIndexOrThrow(_cursor, "profit");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<Load> _result = new ArrayList<Load>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Load _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final String _tmpFromLocation;
            _tmpFromLocation = _cursor.getString(_cursorIndexOfFromLocation);
            final String _tmpToLocation;
            _tmpToLocation = _cursor.getString(_cursorIndexOfToLocation);
            final String _tmpVehicleNumber;
            _tmpVehicleNumber = _cursor.getString(_cursorIndexOfVehicleNumber);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final double _tmpLoadPrice;
            _tmpLoadPrice = _cursor.getDouble(_cursorIndexOfLoadPrice);
            final double _tmpLoadingCharge;
            _tmpLoadingCharge = _cursor.getDouble(_cursorIndexOfLoadingCharge);
            final double _tmpDieselCost;
            _tmpDieselCost = _cursor.getDouble(_cursorIndexOfDieselCost);
            final double _tmpPoliceExpense;
            _tmpPoliceExpense = _cursor.getDouble(_cursorIndexOfPoliceExpense);
            final double _tmpTollFee;
            _tmpTollFee = _cursor.getDouble(_cursorIndexOfTollFee);
            final double _tmpDriverCharge;
            _tmpDriverCharge = _cursor.getDouble(_cursorIndexOfDriverCharge);
            final double _tmpUnloadingCharge;
            _tmpUnloadingCharge = _cursor.getDouble(_cursorIndexOfUnloadingCharge);
            final double _tmpOtherExpense;
            _tmpOtherExpense = _cursor.getDouble(_cursorIndexOfOtherExpense);
            final double _tmpTotalExpense;
            _tmpTotalExpense = _cursor.getDouble(_cursorIndexOfTotalExpense);
            final double _tmpProfit;
            _tmpProfit = _cursor.getDouble(_cursorIndexOfProfit);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new Load(_tmpId,_tmpDate,_tmpFromLocation,_tmpToLocation,_tmpVehicleNumber,_tmpNotes,_tmpLoadPrice,_tmpLoadingCharge,_tmpDieselCost,_tmpPoliceExpense,_tmpTollFee,_tmpDriverCharge,_tmpUnloadingCharge,_tmpOtherExpense,_tmpTotalExpense,_tmpProfit,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<Load>> getLoadsByDateRange(final long startDate, final long endDate) {
    final String _sql = "\n"
            + "        SELECT * FROM loads \n"
            + "        WHERE date BETWEEN ? AND ? \n"
            + "        ORDER BY date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endDate);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"loads"}, new Callable<List<Load>>() {
      @Override
      @NonNull
      public List<Load> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfFromLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "fromLocation");
          final int _cursorIndexOfToLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "toLocation");
          final int _cursorIndexOfVehicleNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "vehicleNumber");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfLoadPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "loadPrice");
          final int _cursorIndexOfLoadingCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "loadingCharge");
          final int _cursorIndexOfDieselCost = CursorUtil.getColumnIndexOrThrow(_cursor, "dieselCost");
          final int _cursorIndexOfPoliceExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "policeExpense");
          final int _cursorIndexOfTollFee = CursorUtil.getColumnIndexOrThrow(_cursor, "tollFee");
          final int _cursorIndexOfDriverCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "driverCharge");
          final int _cursorIndexOfUnloadingCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "unloadingCharge");
          final int _cursorIndexOfOtherExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "otherExpense");
          final int _cursorIndexOfTotalExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "totalExpense");
          final int _cursorIndexOfProfit = CursorUtil.getColumnIndexOrThrow(_cursor, "profit");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<Load> _result = new ArrayList<Load>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Load _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final String _tmpFromLocation;
            _tmpFromLocation = _cursor.getString(_cursorIndexOfFromLocation);
            final String _tmpToLocation;
            _tmpToLocation = _cursor.getString(_cursorIndexOfToLocation);
            final String _tmpVehicleNumber;
            _tmpVehicleNumber = _cursor.getString(_cursorIndexOfVehicleNumber);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final double _tmpLoadPrice;
            _tmpLoadPrice = _cursor.getDouble(_cursorIndexOfLoadPrice);
            final double _tmpLoadingCharge;
            _tmpLoadingCharge = _cursor.getDouble(_cursorIndexOfLoadingCharge);
            final double _tmpDieselCost;
            _tmpDieselCost = _cursor.getDouble(_cursorIndexOfDieselCost);
            final double _tmpPoliceExpense;
            _tmpPoliceExpense = _cursor.getDouble(_cursorIndexOfPoliceExpense);
            final double _tmpTollFee;
            _tmpTollFee = _cursor.getDouble(_cursorIndexOfTollFee);
            final double _tmpDriverCharge;
            _tmpDriverCharge = _cursor.getDouble(_cursorIndexOfDriverCharge);
            final double _tmpUnloadingCharge;
            _tmpUnloadingCharge = _cursor.getDouble(_cursorIndexOfUnloadingCharge);
            final double _tmpOtherExpense;
            _tmpOtherExpense = _cursor.getDouble(_cursorIndexOfOtherExpense);
            final double _tmpTotalExpense;
            _tmpTotalExpense = _cursor.getDouble(_cursorIndexOfTotalExpense);
            final double _tmpProfit;
            _tmpProfit = _cursor.getDouble(_cursorIndexOfProfit);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new Load(_tmpId,_tmpDate,_tmpFromLocation,_tmpToLocation,_tmpVehicleNumber,_tmpNotes,_tmpLoadPrice,_tmpLoadingCharge,_tmpDieselCost,_tmpPoliceExpense,_tmpTollFee,_tmpDriverCharge,_tmpUnloadingCharge,_tmpOtherExpense,_tmpTotalExpense,_tmpProfit,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<Load>> getLoadsByMonth(final String yearMonth) {
    final String _sql = "\n"
            + "        SELECT * FROM loads \n"
            + "        WHERE strftime('%Y-%m', date/1000, 'unixepoch') = ?\n"
            + "        ORDER BY date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, yearMonth);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"loads"}, new Callable<List<Load>>() {
      @Override
      @NonNull
      public List<Load> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfFromLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "fromLocation");
          final int _cursorIndexOfToLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "toLocation");
          final int _cursorIndexOfVehicleNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "vehicleNumber");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfLoadPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "loadPrice");
          final int _cursorIndexOfLoadingCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "loadingCharge");
          final int _cursorIndexOfDieselCost = CursorUtil.getColumnIndexOrThrow(_cursor, "dieselCost");
          final int _cursorIndexOfPoliceExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "policeExpense");
          final int _cursorIndexOfTollFee = CursorUtil.getColumnIndexOrThrow(_cursor, "tollFee");
          final int _cursorIndexOfDriverCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "driverCharge");
          final int _cursorIndexOfUnloadingCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "unloadingCharge");
          final int _cursorIndexOfOtherExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "otherExpense");
          final int _cursorIndexOfTotalExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "totalExpense");
          final int _cursorIndexOfProfit = CursorUtil.getColumnIndexOrThrow(_cursor, "profit");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<Load> _result = new ArrayList<Load>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Load _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final String _tmpFromLocation;
            _tmpFromLocation = _cursor.getString(_cursorIndexOfFromLocation);
            final String _tmpToLocation;
            _tmpToLocation = _cursor.getString(_cursorIndexOfToLocation);
            final String _tmpVehicleNumber;
            _tmpVehicleNumber = _cursor.getString(_cursorIndexOfVehicleNumber);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final double _tmpLoadPrice;
            _tmpLoadPrice = _cursor.getDouble(_cursorIndexOfLoadPrice);
            final double _tmpLoadingCharge;
            _tmpLoadingCharge = _cursor.getDouble(_cursorIndexOfLoadingCharge);
            final double _tmpDieselCost;
            _tmpDieselCost = _cursor.getDouble(_cursorIndexOfDieselCost);
            final double _tmpPoliceExpense;
            _tmpPoliceExpense = _cursor.getDouble(_cursorIndexOfPoliceExpense);
            final double _tmpTollFee;
            _tmpTollFee = _cursor.getDouble(_cursorIndexOfTollFee);
            final double _tmpDriverCharge;
            _tmpDriverCharge = _cursor.getDouble(_cursorIndexOfDriverCharge);
            final double _tmpUnloadingCharge;
            _tmpUnloadingCharge = _cursor.getDouble(_cursorIndexOfUnloadingCharge);
            final double _tmpOtherExpense;
            _tmpOtherExpense = _cursor.getDouble(_cursorIndexOfOtherExpense);
            final double _tmpTotalExpense;
            _tmpTotalExpense = _cursor.getDouble(_cursorIndexOfTotalExpense);
            final double _tmpProfit;
            _tmpProfit = _cursor.getDouble(_cursorIndexOfProfit);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new Load(_tmpId,_tmpDate,_tmpFromLocation,_tmpToLocation,_tmpVehicleNumber,_tmpNotes,_tmpLoadPrice,_tmpLoadingCharge,_tmpDieselCost,_tmpPoliceExpense,_tmpTollFee,_tmpDriverCharge,_tmpUnloadingCharge,_tmpOtherExpense,_tmpTotalExpense,_tmpProfit,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<Load>> getLoadsByYear(final String year) {
    final String _sql = "\n"
            + "        SELECT * FROM loads \n"
            + "        WHERE strftime('%Y', date/1000, 'unixepoch') = ?\n"
            + "        ORDER BY date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, year);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"loads"}, new Callable<List<Load>>() {
      @Override
      @NonNull
      public List<Load> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfFromLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "fromLocation");
          final int _cursorIndexOfToLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "toLocation");
          final int _cursorIndexOfVehicleNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "vehicleNumber");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfLoadPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "loadPrice");
          final int _cursorIndexOfLoadingCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "loadingCharge");
          final int _cursorIndexOfDieselCost = CursorUtil.getColumnIndexOrThrow(_cursor, "dieselCost");
          final int _cursorIndexOfPoliceExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "policeExpense");
          final int _cursorIndexOfTollFee = CursorUtil.getColumnIndexOrThrow(_cursor, "tollFee");
          final int _cursorIndexOfDriverCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "driverCharge");
          final int _cursorIndexOfUnloadingCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "unloadingCharge");
          final int _cursorIndexOfOtherExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "otherExpense");
          final int _cursorIndexOfTotalExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "totalExpense");
          final int _cursorIndexOfProfit = CursorUtil.getColumnIndexOrThrow(_cursor, "profit");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<Load> _result = new ArrayList<Load>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Load _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final String _tmpFromLocation;
            _tmpFromLocation = _cursor.getString(_cursorIndexOfFromLocation);
            final String _tmpToLocation;
            _tmpToLocation = _cursor.getString(_cursorIndexOfToLocation);
            final String _tmpVehicleNumber;
            _tmpVehicleNumber = _cursor.getString(_cursorIndexOfVehicleNumber);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final double _tmpLoadPrice;
            _tmpLoadPrice = _cursor.getDouble(_cursorIndexOfLoadPrice);
            final double _tmpLoadingCharge;
            _tmpLoadingCharge = _cursor.getDouble(_cursorIndexOfLoadingCharge);
            final double _tmpDieselCost;
            _tmpDieselCost = _cursor.getDouble(_cursorIndexOfDieselCost);
            final double _tmpPoliceExpense;
            _tmpPoliceExpense = _cursor.getDouble(_cursorIndexOfPoliceExpense);
            final double _tmpTollFee;
            _tmpTollFee = _cursor.getDouble(_cursorIndexOfTollFee);
            final double _tmpDriverCharge;
            _tmpDriverCharge = _cursor.getDouble(_cursorIndexOfDriverCharge);
            final double _tmpUnloadingCharge;
            _tmpUnloadingCharge = _cursor.getDouble(_cursorIndexOfUnloadingCharge);
            final double _tmpOtherExpense;
            _tmpOtherExpense = _cursor.getDouble(_cursorIndexOfOtherExpense);
            final double _tmpTotalExpense;
            _tmpTotalExpense = _cursor.getDouble(_cursorIndexOfTotalExpense);
            final double _tmpProfit;
            _tmpProfit = _cursor.getDouble(_cursorIndexOfProfit);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new Load(_tmpId,_tmpDate,_tmpFromLocation,_tmpToLocation,_tmpVehicleNumber,_tmpNotes,_tmpLoadPrice,_tmpLoadingCharge,_tmpDieselCost,_tmpPoliceExpense,_tmpTollFee,_tmpDriverCharge,_tmpUnloadingCharge,_tmpOtherExpense,_tmpTotalExpense,_tmpProfit,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getTodayRevenue(final long startOfDay, final long endOfDay,
      final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(loadPrice), 0) FROM loads \n"
            + "        WHERE date BETWEEN ? AND ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startOfDay);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endOfDay);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
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
  public Object getTodayProfit(final long startOfDay, final long endOfDay,
      final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(profit), 0) FROM loads \n"
            + "        WHERE date BETWEEN ? AND ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startOfDay);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endOfDay);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
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
  public Object getMonthRevenue(final long startOfMonth, final long endOfMonth,
      final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(loadPrice), 0) FROM loads \n"
            + "        WHERE date BETWEEN ? AND ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startOfMonth);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endOfMonth);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
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
  public Object getMonthExpense(final long startOfMonth, final long endOfMonth,
      final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(totalExpense), 0) FROM loads \n"
            + "        WHERE date BETWEEN ? AND ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startOfMonth);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endOfMonth);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
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
  public Object getMonthProfit(final long startOfMonth, final long endOfMonth,
      final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(profit), 0) FROM loads \n"
            + "        WHERE date BETWEEN ? AND ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startOfMonth);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endOfMonth);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
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
  public Object getMonthLoadCount(final long startOfMonth, final long endOfMonth,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM loads \n"
            + "        WHERE date BETWEEN ? AND ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startOfMonth);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endOfMonth);
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

  @Override
  public Object getYearRevenue(final long startOfYear, final long endOfYear,
      final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(loadPrice), 0) FROM loads \n"
            + "        WHERE date BETWEEN ? AND ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startOfYear);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endOfYear);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
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
  public Object getYearExpense(final long startOfYear, final long endOfYear,
      final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(totalExpense), 0) FROM loads \n"
            + "        WHERE date BETWEEN ? AND ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startOfYear);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endOfYear);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
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
  public Object getYearProfit(final long startOfYear, final long endOfYear,
      final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(profit), 0) FROM loads \n"
            + "        WHERE date BETWEEN ? AND ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startOfYear);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endOfYear);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
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
  public Object getYearLoadCount(final long startOfYear, final long endOfYear,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM loads \n"
            + "        WHERE date BETWEEN ? AND ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startOfYear);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endOfYear);
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

  @Override
  public LiveData<Integer> getTotalLoadCount() {
    final String _sql = "SELECT COUNT(*) FROM loads";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"loads"}, false, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
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
  public LiveData<Double> getTotalRevenue() {
    final String _sql = "SELECT COALESCE(SUM(loadPrice), 0) FROM loads";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"loads"}, false, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
          } else {
            _result = null;
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
  public LiveData<Double> getTotalProfit() {
    final String _sql = "SELECT COALESCE(SUM(profit), 0) FROM loads";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"loads"}, false, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
          } else {
            _result = null;
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
  public Object getMonthlyTrend(final long since,
      final Continuation<? super List<MonthlyTrendData>> $completion) {
    final String _sql = "\n"
            + "        SELECT \n"
            + "            strftime('%Y-%m', date/1000, 'unixepoch') as month,\n"
            + "            COALESCE(SUM(loadPrice), 0) as revenue,\n"
            + "            COALESCE(SUM(totalExpense), 0) as expense,\n"
            + "            COALESCE(SUM(profit), 0) as profit,\n"
            + "            COUNT(*) as loadCount\n"
            + "        FROM loads\n"
            + "        WHERE date >= ?\n"
            + "        GROUP BY month\n"
            + "        ORDER BY month ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, since);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MonthlyTrendData>>() {
      @Override
      @NonNull
      public List<MonthlyTrendData> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMonth = 0;
          final int _cursorIndexOfRevenue = 1;
          final int _cursorIndexOfExpense = 2;
          final int _cursorIndexOfProfit = 3;
          final int _cursorIndexOfLoadCount = 4;
          final List<MonthlyTrendData> _result = new ArrayList<MonthlyTrendData>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MonthlyTrendData _item;
            final String _tmpMonth;
            _tmpMonth = _cursor.getString(_cursorIndexOfMonth);
            final double _tmpRevenue;
            _tmpRevenue = _cursor.getDouble(_cursorIndexOfRevenue);
            final double _tmpExpense;
            _tmpExpense = _cursor.getDouble(_cursorIndexOfExpense);
            final double _tmpProfit;
            _tmpProfit = _cursor.getDouble(_cursorIndexOfProfit);
            final int _tmpLoadCount;
            _tmpLoadCount = _cursor.getInt(_cursorIndexOfLoadCount);
            _item = new MonthlyTrendData(_tmpMonth,_tmpRevenue,_tmpExpense,_tmpProfit,_tmpLoadCount);
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
  public Object getAllLoadsForExport(final Continuation<? super List<Load>> $completion) {
    final String _sql = "SELECT * FROM loads ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Load>>() {
      @Override
      @NonNull
      public List<Load> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfFromLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "fromLocation");
          final int _cursorIndexOfToLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "toLocation");
          final int _cursorIndexOfVehicleNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "vehicleNumber");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfLoadPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "loadPrice");
          final int _cursorIndexOfLoadingCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "loadingCharge");
          final int _cursorIndexOfDieselCost = CursorUtil.getColumnIndexOrThrow(_cursor, "dieselCost");
          final int _cursorIndexOfPoliceExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "policeExpense");
          final int _cursorIndexOfTollFee = CursorUtil.getColumnIndexOrThrow(_cursor, "tollFee");
          final int _cursorIndexOfDriverCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "driverCharge");
          final int _cursorIndexOfUnloadingCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "unloadingCharge");
          final int _cursorIndexOfOtherExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "otherExpense");
          final int _cursorIndexOfTotalExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "totalExpense");
          final int _cursorIndexOfProfit = CursorUtil.getColumnIndexOrThrow(_cursor, "profit");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<Load> _result = new ArrayList<Load>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Load _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final String _tmpFromLocation;
            _tmpFromLocation = _cursor.getString(_cursorIndexOfFromLocation);
            final String _tmpToLocation;
            _tmpToLocation = _cursor.getString(_cursorIndexOfToLocation);
            final String _tmpVehicleNumber;
            _tmpVehicleNumber = _cursor.getString(_cursorIndexOfVehicleNumber);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final double _tmpLoadPrice;
            _tmpLoadPrice = _cursor.getDouble(_cursorIndexOfLoadPrice);
            final double _tmpLoadingCharge;
            _tmpLoadingCharge = _cursor.getDouble(_cursorIndexOfLoadingCharge);
            final double _tmpDieselCost;
            _tmpDieselCost = _cursor.getDouble(_cursorIndexOfDieselCost);
            final double _tmpPoliceExpense;
            _tmpPoliceExpense = _cursor.getDouble(_cursorIndexOfPoliceExpense);
            final double _tmpTollFee;
            _tmpTollFee = _cursor.getDouble(_cursorIndexOfTollFee);
            final double _tmpDriverCharge;
            _tmpDriverCharge = _cursor.getDouble(_cursorIndexOfDriverCharge);
            final double _tmpUnloadingCharge;
            _tmpUnloadingCharge = _cursor.getDouble(_cursorIndexOfUnloadingCharge);
            final double _tmpOtherExpense;
            _tmpOtherExpense = _cursor.getDouble(_cursorIndexOfOtherExpense);
            final double _tmpTotalExpense;
            _tmpTotalExpense = _cursor.getDouble(_cursorIndexOfTotalExpense);
            final double _tmpProfit;
            _tmpProfit = _cursor.getDouble(_cursorIndexOfProfit);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new Load(_tmpId,_tmpDate,_tmpFromLocation,_tmpToLocation,_tmpVehicleNumber,_tmpNotes,_tmpLoadPrice,_tmpLoadingCharge,_tmpDieselCost,_tmpPoliceExpense,_tmpTollFee,_tmpDriverCharge,_tmpUnloadingCharge,_tmpOtherExpense,_tmpTotalExpense,_tmpProfit,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getLoadsByDateRangeForExport(final long startDate, final long endDate,
      final Continuation<? super List<Load>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM loads \n"
            + "        WHERE date BETWEEN ? AND ? \n"
            + "        ORDER BY date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startDate);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endDate);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Load>>() {
      @Override
      @NonNull
      public List<Load> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfFromLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "fromLocation");
          final int _cursorIndexOfToLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "toLocation");
          final int _cursorIndexOfVehicleNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "vehicleNumber");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfLoadPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "loadPrice");
          final int _cursorIndexOfLoadingCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "loadingCharge");
          final int _cursorIndexOfDieselCost = CursorUtil.getColumnIndexOrThrow(_cursor, "dieselCost");
          final int _cursorIndexOfPoliceExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "policeExpense");
          final int _cursorIndexOfTollFee = CursorUtil.getColumnIndexOrThrow(_cursor, "tollFee");
          final int _cursorIndexOfDriverCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "driverCharge");
          final int _cursorIndexOfUnloadingCharge = CursorUtil.getColumnIndexOrThrow(_cursor, "unloadingCharge");
          final int _cursorIndexOfOtherExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "otherExpense");
          final int _cursorIndexOfTotalExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "totalExpense");
          final int _cursorIndexOfProfit = CursorUtil.getColumnIndexOrThrow(_cursor, "profit");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<Load> _result = new ArrayList<Load>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Load _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final String _tmpFromLocation;
            _tmpFromLocation = _cursor.getString(_cursorIndexOfFromLocation);
            final String _tmpToLocation;
            _tmpToLocation = _cursor.getString(_cursorIndexOfToLocation);
            final String _tmpVehicleNumber;
            _tmpVehicleNumber = _cursor.getString(_cursorIndexOfVehicleNumber);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final double _tmpLoadPrice;
            _tmpLoadPrice = _cursor.getDouble(_cursorIndexOfLoadPrice);
            final double _tmpLoadingCharge;
            _tmpLoadingCharge = _cursor.getDouble(_cursorIndexOfLoadingCharge);
            final double _tmpDieselCost;
            _tmpDieselCost = _cursor.getDouble(_cursorIndexOfDieselCost);
            final double _tmpPoliceExpense;
            _tmpPoliceExpense = _cursor.getDouble(_cursorIndexOfPoliceExpense);
            final double _tmpTollFee;
            _tmpTollFee = _cursor.getDouble(_cursorIndexOfTollFee);
            final double _tmpDriverCharge;
            _tmpDriverCharge = _cursor.getDouble(_cursorIndexOfDriverCharge);
            final double _tmpUnloadingCharge;
            _tmpUnloadingCharge = _cursor.getDouble(_cursorIndexOfUnloadingCharge);
            final double _tmpOtherExpense;
            _tmpOtherExpense = _cursor.getDouble(_cursorIndexOfOtherExpense);
            final double _tmpTotalExpense;
            _tmpTotalExpense = _cursor.getDouble(_cursorIndexOfTotalExpense);
            final double _tmpProfit;
            _tmpProfit = _cursor.getDouble(_cursorIndexOfProfit);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new Load(_tmpId,_tmpDate,_tmpFromLocation,_tmpToLocation,_tmpVehicleNumber,_tmpNotes,_tmpLoadPrice,_tmpLoadingCharge,_tmpDieselCost,_tmpPoliceExpense,_tmpTollFee,_tmpDriverCharge,_tmpUnloadingCharge,_tmpOtherExpense,_tmpTotalExpense,_tmpProfit,_tmpCreatedAt,_tmpUpdatedAt);
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
