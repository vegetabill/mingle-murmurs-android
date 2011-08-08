package com.dephillipsdesign.android;

import com.thoughtworks.mingle.murmurs.MurmursLoader.NoMoreMurmursException;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.MatrixCursor;

public class PaginatedHttpDataCursor extends CursorWrapper {

  public interface PaginatedDataRetriever {
    MatrixCursor populateInitialPage();
    void addPageToCursor(int pageNumber, MatrixCursor cursor);
  }
  
  private int page = 1;
  private MatrixCursor internalCursor;
  private final PaginatedDataRetriever paginatedDataRetriever;
  
  private PaginatedHttpDataCursor(Cursor cursor, PaginatedDataRetriever paginatedDataRetriever) {
    super(cursor);
    this.paginatedDataRetriever = paginatedDataRetriever;
  }

  public int getCount() {
    return 100000;
  }
  
  public static PaginatedHttpDataCursor create(PaginatedDataRetriever dataManager) {
    MatrixCursor internalCursor = dataManager.populateInitialPage(); //TODO make this lazy
    PaginatedHttpDataCursor restCursor = new PaginatedHttpDataCursor(internalCursor, dataManager);
    restCursor.internalCursor = internalCursor;
    return restCursor;
  }
  
  public boolean moveToPosition(int position) {
    while (position > internalCursor.getCount() - 1) {
      try {
        loadAnotherPageOfResults();
      } catch (Exception e) {
        return false;
      }
    }
    return super.moveToPosition(position);
  }

  private void loadAnotherPageOfResults() {
    int newPage = this.page + 1;
    this.paginatedDataRetriever.addPageToCursor(newPage, internalCursor);
    this.page = newPage;
  }
  
}
