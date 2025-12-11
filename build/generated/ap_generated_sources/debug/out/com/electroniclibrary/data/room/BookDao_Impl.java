package com.electroniclibrary.data.room;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BookDao_Impl implements BookDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BookEntity> __insertionAdapterOfBookEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public BookDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBookEntity = new EntityInsertionAdapter<BookEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `books` (`id`,`title`,`authorId`,`genreId`,`description`,`coverUrl`,`fileUrl`,`rating`,`ratingCount`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final BookEntity entity) {
        if (entity.id == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.id);
        }
        if (entity.title == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.title);
        }
        if (entity.authorId == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.authorId);
        }
        if (entity.genreId == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.genreId);
        }
        if (entity.description == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.description);
        }
        if (entity.coverUrl == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.coverUrl);
        }
        if (entity.fileUrl == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.fileUrl);
        }
        if (entity.rating == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.rating);
        }
        if (entity.ratingCount == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.ratingCount);
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM books";
        return _query;
      }
    };
  }

  @Override
  public void insertAll(final List<BookEntity> books) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfBookEntity.insert(books);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insert(final BookEntity book) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfBookEntity.insert(book);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public List<BookEntity> getAllBooks() {
    final String _sql = "SELECT * FROM books";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfAuthorId = CursorUtil.getColumnIndexOrThrow(_cursor, "authorId");
      final int _cursorIndexOfGenreId = CursorUtil.getColumnIndexOrThrow(_cursor, "genreId");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfCoverUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "coverUrl");
      final int _cursorIndexOfFileUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "fileUrl");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfRatingCount = CursorUtil.getColumnIndexOrThrow(_cursor, "ratingCount");
      final List<BookEntity> _result = new ArrayList<BookEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final BookEntity _item;
        _item = new BookEntity();
        if (_cursor.isNull(_cursorIndexOfId)) {
          _item.id = null;
        } else {
          _item.id = _cursor.getString(_cursorIndexOfId);
        }
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _item.title = null;
        } else {
          _item.title = _cursor.getString(_cursorIndexOfTitle);
        }
        if (_cursor.isNull(_cursorIndexOfAuthorId)) {
          _item.authorId = null;
        } else {
          _item.authorId = _cursor.getString(_cursorIndexOfAuthorId);
        }
        if (_cursor.isNull(_cursorIndexOfGenreId)) {
          _item.genreId = null;
        } else {
          _item.genreId = _cursor.getString(_cursorIndexOfGenreId);
        }
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _item.description = null;
        } else {
          _item.description = _cursor.getString(_cursorIndexOfDescription);
        }
        if (_cursor.isNull(_cursorIndexOfCoverUrl)) {
          _item.coverUrl = null;
        } else {
          _item.coverUrl = _cursor.getString(_cursorIndexOfCoverUrl);
        }
        if (_cursor.isNull(_cursorIndexOfFileUrl)) {
          _item.fileUrl = null;
        } else {
          _item.fileUrl = _cursor.getString(_cursorIndexOfFileUrl);
        }
        if (_cursor.isNull(_cursorIndexOfRating)) {
          _item.rating = null;
        } else {
          _item.rating = _cursor.getDouble(_cursorIndexOfRating);
        }
        if (_cursor.isNull(_cursorIndexOfRatingCount)) {
          _item.ratingCount = null;
        } else {
          _item.ratingCount = _cursor.getInt(_cursorIndexOfRatingCount);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public BookEntity getBookById(final String bookId) {
    final String _sql = "SELECT * FROM books WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (bookId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, bookId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfAuthorId = CursorUtil.getColumnIndexOrThrow(_cursor, "authorId");
      final int _cursorIndexOfGenreId = CursorUtil.getColumnIndexOrThrow(_cursor, "genreId");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfCoverUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "coverUrl");
      final int _cursorIndexOfFileUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "fileUrl");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfRatingCount = CursorUtil.getColumnIndexOrThrow(_cursor, "ratingCount");
      final BookEntity _result;
      if (_cursor.moveToFirst()) {
        _result = new BookEntity();
        if (_cursor.isNull(_cursorIndexOfId)) {
          _result.id = null;
        } else {
          _result.id = _cursor.getString(_cursorIndexOfId);
        }
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _result.title = null;
        } else {
          _result.title = _cursor.getString(_cursorIndexOfTitle);
        }
        if (_cursor.isNull(_cursorIndexOfAuthorId)) {
          _result.authorId = null;
        } else {
          _result.authorId = _cursor.getString(_cursorIndexOfAuthorId);
        }
        if (_cursor.isNull(_cursorIndexOfGenreId)) {
          _result.genreId = null;
        } else {
          _result.genreId = _cursor.getString(_cursorIndexOfGenreId);
        }
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _result.description = null;
        } else {
          _result.description = _cursor.getString(_cursorIndexOfDescription);
        }
        if (_cursor.isNull(_cursorIndexOfCoverUrl)) {
          _result.coverUrl = null;
        } else {
          _result.coverUrl = _cursor.getString(_cursorIndexOfCoverUrl);
        }
        if (_cursor.isNull(_cursorIndexOfFileUrl)) {
          _result.fileUrl = null;
        } else {
          _result.fileUrl = _cursor.getString(_cursorIndexOfFileUrl);
        }
        if (_cursor.isNull(_cursorIndexOfRating)) {
          _result.rating = null;
        } else {
          _result.rating = _cursor.getDouble(_cursorIndexOfRating);
        }
        if (_cursor.isNull(_cursorIndexOfRatingCount)) {
          _result.ratingCount = null;
        } else {
          _result.ratingCount = _cursor.getInt(_cursorIndexOfRatingCount);
        }
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<BookEntity> getBooksByGenre(final String genreId) {
    final String _sql = "SELECT * FROM books WHERE genreId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (genreId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, genreId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfAuthorId = CursorUtil.getColumnIndexOrThrow(_cursor, "authorId");
      final int _cursorIndexOfGenreId = CursorUtil.getColumnIndexOrThrow(_cursor, "genreId");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfCoverUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "coverUrl");
      final int _cursorIndexOfFileUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "fileUrl");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfRatingCount = CursorUtil.getColumnIndexOrThrow(_cursor, "ratingCount");
      final List<BookEntity> _result = new ArrayList<BookEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final BookEntity _item;
        _item = new BookEntity();
        if (_cursor.isNull(_cursorIndexOfId)) {
          _item.id = null;
        } else {
          _item.id = _cursor.getString(_cursorIndexOfId);
        }
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _item.title = null;
        } else {
          _item.title = _cursor.getString(_cursorIndexOfTitle);
        }
        if (_cursor.isNull(_cursorIndexOfAuthorId)) {
          _item.authorId = null;
        } else {
          _item.authorId = _cursor.getString(_cursorIndexOfAuthorId);
        }
        if (_cursor.isNull(_cursorIndexOfGenreId)) {
          _item.genreId = null;
        } else {
          _item.genreId = _cursor.getString(_cursorIndexOfGenreId);
        }
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _item.description = null;
        } else {
          _item.description = _cursor.getString(_cursorIndexOfDescription);
        }
        if (_cursor.isNull(_cursorIndexOfCoverUrl)) {
          _item.coverUrl = null;
        } else {
          _item.coverUrl = _cursor.getString(_cursorIndexOfCoverUrl);
        }
        if (_cursor.isNull(_cursorIndexOfFileUrl)) {
          _item.fileUrl = null;
        } else {
          _item.fileUrl = _cursor.getString(_cursorIndexOfFileUrl);
        }
        if (_cursor.isNull(_cursorIndexOfRating)) {
          _item.rating = null;
        } else {
          _item.rating = _cursor.getDouble(_cursorIndexOfRating);
        }
        if (_cursor.isNull(_cursorIndexOfRatingCount)) {
          _item.ratingCount = null;
        } else {
          _item.ratingCount = _cursor.getInt(_cursorIndexOfRatingCount);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<BookEntity> getBooksByAuthor(final String authorId) {
    final String _sql = "SELECT * FROM books WHERE authorId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (authorId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, authorId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfAuthorId = CursorUtil.getColumnIndexOrThrow(_cursor, "authorId");
      final int _cursorIndexOfGenreId = CursorUtil.getColumnIndexOrThrow(_cursor, "genreId");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfCoverUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "coverUrl");
      final int _cursorIndexOfFileUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "fileUrl");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfRatingCount = CursorUtil.getColumnIndexOrThrow(_cursor, "ratingCount");
      final List<BookEntity> _result = new ArrayList<BookEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final BookEntity _item;
        _item = new BookEntity();
        if (_cursor.isNull(_cursorIndexOfId)) {
          _item.id = null;
        } else {
          _item.id = _cursor.getString(_cursorIndexOfId);
        }
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _item.title = null;
        } else {
          _item.title = _cursor.getString(_cursorIndexOfTitle);
        }
        if (_cursor.isNull(_cursorIndexOfAuthorId)) {
          _item.authorId = null;
        } else {
          _item.authorId = _cursor.getString(_cursorIndexOfAuthorId);
        }
        if (_cursor.isNull(_cursorIndexOfGenreId)) {
          _item.genreId = null;
        } else {
          _item.genreId = _cursor.getString(_cursorIndexOfGenreId);
        }
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _item.description = null;
        } else {
          _item.description = _cursor.getString(_cursorIndexOfDescription);
        }
        if (_cursor.isNull(_cursorIndexOfCoverUrl)) {
          _item.coverUrl = null;
        } else {
          _item.coverUrl = _cursor.getString(_cursorIndexOfCoverUrl);
        }
        if (_cursor.isNull(_cursorIndexOfFileUrl)) {
          _item.fileUrl = null;
        } else {
          _item.fileUrl = _cursor.getString(_cursorIndexOfFileUrl);
        }
        if (_cursor.isNull(_cursorIndexOfRating)) {
          _item.rating = null;
        } else {
          _item.rating = _cursor.getDouble(_cursorIndexOfRating);
        }
        if (_cursor.isNull(_cursorIndexOfRatingCount)) {
          _item.ratingCount = null;
        } else {
          _item.ratingCount = _cursor.getInt(_cursorIndexOfRatingCount);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
