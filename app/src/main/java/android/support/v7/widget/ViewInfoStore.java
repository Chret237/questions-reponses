package android.support.v7.widget;

import android.support.v4.util.ArrayMap;
import android.support.v4.util.LongSparseArray;
import android.support.v4.util.Pools;
import android.support.v7.widget.RecyclerView;

/* loaded from: classes-dex2jar.jar:android/support/v7/widget/ViewInfoStore.class */
class ViewInfoStore {
    private static final boolean DEBUG = false;
    final ArrayMap<RecyclerView.ViewHolder, InfoRecord> mLayoutHolderMap = new ArrayMap<>();
    final LongSparseArray<RecyclerView.ViewHolder> mOldChangedHolders = new LongSparseArray<>();

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/ViewInfoStore$InfoRecord.class */
    static class InfoRecord {
        static final int FLAG_APPEAR = 2;
        static final int FLAG_APPEAR_AND_DISAPPEAR = 3;
        static final int FLAG_APPEAR_PRE_AND_POST = 14;
        static final int FLAG_DISAPPEARED = 1;
        static final int FLAG_POST = 8;
        static final int FLAG_PRE = 4;
        static final int FLAG_PRE_AND_POST = 12;
        static Pools.Pool<InfoRecord> sPool = new Pools.SimplePool(20);
        int flags;
        RecyclerView.ItemAnimator.ItemHolderInfo postInfo;
        RecyclerView.ItemAnimator.ItemHolderInfo preInfo;

        private InfoRecord() {
        }

        static void drainCache() {
            while (sPool.acquire() != null) {
            }
        }

        static InfoRecord obtain() {
            InfoRecord infoRecordAcquire = sPool.acquire();
            InfoRecord infoRecord = infoRecordAcquire;
            if (infoRecordAcquire == null) {
                infoRecord = new InfoRecord();
            }
            return infoRecord;
        }

        static void recycle(InfoRecord infoRecord) {
            infoRecord.flags = 0;
            infoRecord.preInfo = null;
            infoRecord.postInfo = null;
            sPool.release(infoRecord);
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/ViewInfoStore$ProcessCallback.class */
    interface ProcessCallback {
        void processAppeared(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo2);

        void processDisappeared(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo2);

        void processPersistent(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo2);

        void unused(RecyclerView.ViewHolder viewHolder);
    }

    ViewInfoStore() {
    }

    private RecyclerView.ItemAnimator.ItemHolderInfo popFromLayoutStep(RecyclerView.ViewHolder viewHolder, int i) {
        InfoRecord infoRecordValueAt;
        RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo;
        int iIndexOfKey = this.mLayoutHolderMap.indexOfKey(viewHolder);
        if (iIndexOfKey < 0 || (infoRecordValueAt = this.mLayoutHolderMap.valueAt(iIndexOfKey)) == null || (infoRecordValueAt.flags & i) == 0) {
            return null;
        }
        infoRecordValueAt.flags &= i ^ (-1);
        if (i == 4) {
            itemHolderInfo = infoRecordValueAt.preInfo;
        } else {
            if (i != 8) {
                throw new IllegalArgumentException("Must provide flag PRE or POST");
            }
            itemHolderInfo = infoRecordValueAt.postInfo;
        }
        if ((infoRecordValueAt.flags & 12) == 0) {
            this.mLayoutHolderMap.removeAt(iIndexOfKey);
            InfoRecord.recycle(infoRecordValueAt);
        }
        return itemHolderInfo;
    }

    void addToAppearedInPreLayoutHolders(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo) {
        InfoRecord infoRecord = this.mLayoutHolderMap.get(viewHolder);
        InfoRecord infoRecordObtain = infoRecord;
        if (infoRecord == null) {
            infoRecordObtain = InfoRecord.obtain();
            this.mLayoutHolderMap.put(viewHolder, infoRecordObtain);
        }
        infoRecordObtain.flags |= 2;
        infoRecordObtain.preInfo = itemHolderInfo;
    }

    void addToDisappearedInLayout(RecyclerView.ViewHolder viewHolder) {
        InfoRecord infoRecord = this.mLayoutHolderMap.get(viewHolder);
        InfoRecord infoRecordObtain = infoRecord;
        if (infoRecord == null) {
            infoRecordObtain = InfoRecord.obtain();
            this.mLayoutHolderMap.put(viewHolder, infoRecordObtain);
        }
        infoRecordObtain.flags |= 1;
    }

    void addToOldChangeHolders(long j, RecyclerView.ViewHolder viewHolder) {
        this.mOldChangedHolders.put(j, viewHolder);
    }

    void addToPostLayout(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo) {
        InfoRecord infoRecord = this.mLayoutHolderMap.get(viewHolder);
        InfoRecord infoRecordObtain = infoRecord;
        if (infoRecord == null) {
            infoRecordObtain = InfoRecord.obtain();
            this.mLayoutHolderMap.put(viewHolder, infoRecordObtain);
        }
        infoRecordObtain.postInfo = itemHolderInfo;
        infoRecordObtain.flags |= 8;
    }

    void addToPreLayout(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo) {
        InfoRecord infoRecord = this.mLayoutHolderMap.get(viewHolder);
        InfoRecord infoRecordObtain = infoRecord;
        if (infoRecord == null) {
            infoRecordObtain = InfoRecord.obtain();
            this.mLayoutHolderMap.put(viewHolder, infoRecordObtain);
        }
        infoRecordObtain.preInfo = itemHolderInfo;
        infoRecordObtain.flags |= 4;
    }

    void clear() {
        this.mLayoutHolderMap.clear();
        this.mOldChangedHolders.clear();
    }

    RecyclerView.ViewHolder getFromOldChangeHolders(long j) {
        return this.mOldChangedHolders.get(j);
    }

    boolean isDisappearing(RecyclerView.ViewHolder viewHolder) {
        InfoRecord infoRecord = this.mLayoutHolderMap.get(viewHolder);
        boolean z = true;
        if (infoRecord == null || (infoRecord.flags & 1) == 0) {
            z = false;
        }
        return z;
    }

    boolean isInPreLayout(RecyclerView.ViewHolder viewHolder) {
        InfoRecord infoRecord = this.mLayoutHolderMap.get(viewHolder);
        return (infoRecord == null || (infoRecord.flags & 4) == 0) ? false : true;
    }

    void onDetach() {
        InfoRecord.drainCache();
    }

    public void onViewDetached(RecyclerView.ViewHolder viewHolder) {
        removeFromDisappearedInLayout(viewHolder);
    }

    RecyclerView.ItemAnimator.ItemHolderInfo popFromPostLayout(RecyclerView.ViewHolder viewHolder) {
        return popFromLayoutStep(viewHolder, 8);
    }

    RecyclerView.ItemAnimator.ItemHolderInfo popFromPreLayout(RecyclerView.ViewHolder viewHolder) {
        return popFromLayoutStep(viewHolder, 4);
    }

    void process(ProcessCallback processCallback) {
        for (int size = this.mLayoutHolderMap.size() - 1; size >= 0; size--) {
            RecyclerView.ViewHolder viewHolderKeyAt = this.mLayoutHolderMap.keyAt(size);
            InfoRecord infoRecordRemoveAt = this.mLayoutHolderMap.removeAt(size);
            if ((infoRecordRemoveAt.flags & 3) == 3) {
                processCallback.unused(viewHolderKeyAt);
            } else if ((infoRecordRemoveAt.flags & 1) != 0) {
                if (infoRecordRemoveAt.preInfo == null) {
                    processCallback.unused(viewHolderKeyAt);
                } else {
                    processCallback.processDisappeared(viewHolderKeyAt, infoRecordRemoveAt.preInfo, infoRecordRemoveAt.postInfo);
                }
            } else if ((infoRecordRemoveAt.flags & 14) == 14) {
                processCallback.processAppeared(viewHolderKeyAt, infoRecordRemoveAt.preInfo, infoRecordRemoveAt.postInfo);
            } else if ((infoRecordRemoveAt.flags & 12) == 12) {
                processCallback.processPersistent(viewHolderKeyAt, infoRecordRemoveAt.preInfo, infoRecordRemoveAt.postInfo);
            } else if ((infoRecordRemoveAt.flags & 4) != 0) {
                processCallback.processDisappeared(viewHolderKeyAt, infoRecordRemoveAt.preInfo, null);
            } else if ((infoRecordRemoveAt.flags & 8) != 0) {
                processCallback.processAppeared(viewHolderKeyAt, infoRecordRemoveAt.preInfo, infoRecordRemoveAt.postInfo);
            } else {
                int i = infoRecordRemoveAt.flags;
            }
            InfoRecord.recycle(infoRecordRemoveAt);
        }
    }

    void removeFromDisappearedInLayout(RecyclerView.ViewHolder viewHolder) {
        InfoRecord infoRecord = this.mLayoutHolderMap.get(viewHolder);
        if (infoRecord == null) {
            return;
        }
        infoRecord.flags &= -2;
    }

    void removeViewHolder(RecyclerView.ViewHolder viewHolder) {
        int size = this.mOldChangedHolders.size() - 1;
        while (true) {
            if (size < 0) {
                break;
            }
            if (viewHolder == this.mOldChangedHolders.valueAt(size)) {
                this.mOldChangedHolders.removeAt(size);
                break;
            }
            size--;
        }
        InfoRecord infoRecordRemove = this.mLayoutHolderMap.remove(viewHolder);
        if (infoRecordRemove != null) {
            InfoRecord.recycle(infoRecordRemove);
        }
    }
}
