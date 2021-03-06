/*
 * Copyright (c) 2017-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package com.facebook.litho.widget;

import static com.facebook.litho.SizeSpec.EXACTLY;
import static com.facebook.litho.SizeSpec.UNSPECIFIED;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import com.facebook.litho.SizeSpec;

public class GridLayoutInfo implements LayoutInfo {

  private final GridLayoutManager mGridLayoutManager;
  private final GridSpanSizeLookup mGridSpanSizeLookup;

  private RenderInfoCollection mRenderInfoCollection;

  public GridLayoutInfo(Context context, int spanCount, int orientation, boolean reverseLayout) {
    mGridLayoutManager = new GridLayoutManager(context, spanCount, orientation, reverseLayout);
    mGridSpanSizeLookup = new GridSpanSizeLookup();
    mGridLayoutManager.setSpanSizeLookup(mGridSpanSizeLookup);
  }

  public GridLayoutInfo(Context context, int spanCount) {
    this(context, spanCount, OrientationHelper.VERTICAL, false);
  }

  @Override
  public int getScrollDirection() {
    return mGridLayoutManager.getOrientation();
  }

  @Override
  public int findFirstVisibleItemPosition() {
    return mGridLayoutManager.findFirstVisibleItemPosition();
  }

  @Override
  public int findLastVisibleItemPosition() {
    return mGridLayoutManager.findLastVisibleItemPosition();
  }

  @Override
  public int findFirstFullyVisibleItemPosition() {
    return mGridLayoutManager.findFirstCompletelyVisibleItemPosition();
  }

  @Override
  public int findLastFullyVisibleItemPosition() {
    return mGridLayoutManager.findLastCompletelyVisibleItemPosition();
  }

  @Override
  public int getItemCount() {
    return mGridLayoutManager.getItemCount();
  }

  @Override
  public RecyclerView.LayoutManager getLayoutManager() {
    return mGridLayoutManager;
  }

  @Override
  public void setRenderInfoCollection(RenderInfoCollection renderInfoCollection) {
    mRenderInfoCollection = renderInfoCollection;
  }

  @Override
  public int approximateRangeSize(
      int firstMeasuredItemWidth,
      int firstMeasuredItemHeight,
      int recyclerMeasuredWidth,
      int recyclerMeasuredHeight) {
    final int spanCount = mGridLayoutManager.getSpanCount();
    switch (mGridLayoutManager.getOrientation()) {
      case GridLayoutManager.HORIZONTAL:
        final int colCount = (int) Math.ceil(
            (double) recyclerMeasuredWidth / (double) firstMeasuredItemWidth);

        return colCount * spanCount;
      default:
        final int rowCount = (int) Math.ceil(
            (double) recyclerMeasuredHeight / (double) firstMeasuredItemHeight);

        return rowCount * spanCount;
    }
  }

  /**
   * @param widthSpec the widthSpec used to measure the parent {@link RecyclerSpec}.
   * @return widthSpec of a child that is of span size 1
   */
  @Override
  public int getChildWidthSpec(int widthSpec, RenderInfo renderInfo) {
    switch (mGridLayoutManager.getOrientation()) {
      case GridLayoutManager.HORIZONTAL:
        return SizeSpec.makeSizeSpec(0, UNSPECIFIED);
      default:
        final int spanCount = mGridLayoutManager.getSpanCount();
        final int spanSize = renderInfo.getSpanSize();

        return spanSize * SizeSpec.makeSizeSpec(SizeSpec.getSize(widthSpec) / spanCount, EXACTLY);
    }
  }

  /**
   * @param heightSpec the heightSpec used to measure the parent {@link RecyclerSpec}.
   * @return heightSpec of a child that is of span size 1
   */
  @Override
  public int getChildHeightSpec(int heightSpec, RenderInfo renderInfo) {
    switch (mGridLayoutManager.getOrientation()) {
      case GridLayoutManager.HORIZONTAL:
        final int spanCount = mGridLayoutManager.getSpanCount();
        final int spanSize = renderInfo.getSpanSize();

        return spanSize * SizeSpec.makeSizeSpec(SizeSpec.getSize(heightSpec) / spanCount, EXACTLY);
      default:
        return SizeSpec.makeSizeSpec(0, UNSPECIFIED);
    }
  }

  private class GridSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    @Override
    public int getSpanSize(int position) {
      if (mRenderInfoCollection == null) {
        return 1;
      }

      return mRenderInfoCollection.getRenderInfoAt(position).getSpanSize();
    }
  }
}
