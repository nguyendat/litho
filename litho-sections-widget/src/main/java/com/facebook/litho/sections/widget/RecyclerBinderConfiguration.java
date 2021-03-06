/*
 * Copyright (c) 2017-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package com.facebook.litho.sections.widget;

import android.support.annotation.Nullable;
import com.facebook.litho.widget.LayoutHandlerFactory;
import com.facebook.litho.widget.RecyclerBinder;

/**
 * Configuration setting for {@link RecyclerBinder}.
 */
public class RecyclerBinderConfiguration {
  private static final double DEFAULT_RANGE = 5.0;
  private final double mRangeRatio;
  private final LayoutHandlerFactory mLayoutHandlerFactory;
  private final boolean mCanPrefetchDisplayLists;

  public RecyclerBinderConfiguration(double rangeRatio) {
    this(rangeRatio, null, false);
  }

  public RecyclerBinderConfiguration(double rangeRatio, LayoutHandlerFactory idleExecutor) {
    this(rangeRatio, idleExecutor, false);
  }

  /**
   * @param rangeRatio
   * Ratio to determine the number of components before and after the
   * {@link android.support.v7.widget.RecyclerView}'s total number of currently visible items
   * to have their Component layout computed ahead of time.
   *
   * e.g total number of visible items = 5
   *     rangeRatio = 10
   *     total number of items before the 1st visible item to be computed = 5 * 10 = 50
   *     total number of items after the last visible item to be computed = 5 * 10 = 50
   *
   * @param idleExecutor
   * This determines the thread on which the Component layout calculation will be processed in.
   * Null means that the computation will be done in the background thread.
   *
   * @param canPrefetchDisplayLists
   * If this is true, displaylists for the Android Views and Drawables that are not collected during
   * the ComponentTree layout computation will be collected whenever the UI thread is free.
   */
  public RecyclerBinderConfiguration(
      double rangeRatio,
      @Nullable LayoutHandlerFactory idleExecutor,
      boolean canPrefetchDisplayLists) {
    mRangeRatio = rangeRatio > 0 ? rangeRatio : DEFAULT_RANGE;
    mLayoutHandlerFactory = idleExecutor;
    mCanPrefetchDisplayLists = canPrefetchDisplayLists;
  }

  public double getRangeRatio() {
    return mRangeRatio;
  }

  public LayoutHandlerFactory getLayoutHandlerFactory() {
    return mLayoutHandlerFactory;
  }

  public boolean canPrefetchDisplayLists() {
    return mCanPrefetchDisplayLists;
  }
}
