package android.support.v7.widget;

import android.support.v7.widget.AdapterHelper;
import java.util.List;

/* loaded from: classes-dex2jar.jar:android/support/v7/widget/OpReorderer.class */
class OpReorderer {
    final Callback mCallback;

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/OpReorderer$Callback.class */
    interface Callback {
        AdapterHelper.UpdateOp obtainUpdateOp(int i, int i2, int i3, Object obj);

        void recycleUpdateOp(AdapterHelper.UpdateOp updateOp);
    }

    OpReorderer(Callback callback) {
        this.mCallback = callback;
    }

    private int getLastMoveOutOfOrder(List<AdapterHelper.UpdateOp> list) {
        boolean z;
        int size = list.size() - 1;
        boolean z2 = false;
        while (true) {
            boolean z3 = z2;
            if (size < 0) {
                return -1;
            }
            if (list.get(size).cmd == 8) {
                z = z3;
                if (z3) {
                    return size;
                }
            } else {
                z = true;
            }
            size--;
            z2 = z;
        }
    }

    private void swapMoveAdd(List<AdapterHelper.UpdateOp> list, int i, AdapterHelper.UpdateOp updateOp, int i2, AdapterHelper.UpdateOp updateOp2) {
        int i3 = updateOp.itemCount < updateOp2.positionStart ? -1 : 0;
        int i4 = i3;
        if (updateOp.positionStart < updateOp2.positionStart) {
            i4 = i3 + 1;
        }
        if (updateOp2.positionStart <= updateOp.positionStart) {
            updateOp.positionStart += updateOp2.itemCount;
        }
        if (updateOp2.positionStart <= updateOp.itemCount) {
            updateOp.itemCount += updateOp2.itemCount;
        }
        updateOp2.positionStart += i4;
        list.set(i, updateOp2);
        list.set(i2, updateOp);
    }

    private void swapMoveOp(List<AdapterHelper.UpdateOp> list, int i, int i2) {
        AdapterHelper.UpdateOp updateOp = list.get(i);
        AdapterHelper.UpdateOp updateOp2 = list.get(i2);
        int i3 = updateOp2.cmd;
        if (i3 == 1) {
            swapMoveAdd(list, i, updateOp, i2, updateOp2);
        } else if (i3 == 2) {
            swapMoveRemove(list, i, updateOp, i2, updateOp2);
        } else {
            if (i3 != 4) {
                return;
            }
            swapMoveUpdate(list, i, updateOp, i2, updateOp2);
        }
    }

    void reorderOps(List<AdapterHelper.UpdateOp> list) {
        while (true) {
            int lastMoveOutOfOrder = getLastMoveOutOfOrder(list);
            if (lastMoveOutOfOrder == -1) {
                return;
            } else {
                swapMoveOp(list, lastMoveOutOfOrder, lastMoveOutOfOrder + 1);
            }
        }
    }

    void swapMoveRemove(List<AdapterHelper.UpdateOp> list, int i, AdapterHelper.UpdateOp updateOp, int i2, AdapterHelper.UpdateOp updateOp2) {
        boolean z;
        boolean z2 = false;
        if (updateOp.positionStart < updateOp.itemCount) {
            if (updateOp2.positionStart == updateOp.positionStart && updateOp2.itemCount == updateOp.itemCount - updateOp.positionStart) {
                z = false;
                z2 = true;
            } else {
                z = false;
            }
        } else if (updateOp2.positionStart == updateOp.itemCount + 1 && updateOp2.itemCount == updateOp.positionStart - updateOp.itemCount) {
            z = true;
            z2 = true;
        } else {
            z = true;
        }
        if (updateOp.itemCount < updateOp2.positionStart) {
            updateOp2.positionStart--;
        } else if (updateOp.itemCount < updateOp2.positionStart + updateOp2.itemCount) {
            updateOp2.itemCount--;
            updateOp.cmd = 2;
            updateOp.itemCount = 1;
            if (updateOp2.itemCount == 0) {
                list.remove(i2);
                this.mCallback.recycleUpdateOp(updateOp2);
                return;
            }
            return;
        }
        AdapterHelper.UpdateOp updateOpObtainUpdateOp = null;
        if (updateOp.positionStart <= updateOp2.positionStart) {
            updateOp2.positionStart++;
        } else if (updateOp.positionStart < updateOp2.positionStart + updateOp2.itemCount) {
            updateOpObtainUpdateOp = this.mCallback.obtainUpdateOp(2, updateOp.positionStart + 1, (updateOp2.positionStart + updateOp2.itemCount) - updateOp.positionStart, null);
            updateOp2.itemCount = updateOp.positionStart - updateOp2.positionStart;
        }
        if (z2) {
            list.set(i, updateOp2);
            list.remove(i2);
            this.mCallback.recycleUpdateOp(updateOp);
            return;
        }
        if (z) {
            if (updateOpObtainUpdateOp != null) {
                if (updateOp.positionStart > updateOpObtainUpdateOp.positionStart) {
                    updateOp.positionStart -= updateOpObtainUpdateOp.itemCount;
                }
                if (updateOp.itemCount > updateOpObtainUpdateOp.positionStart) {
                    updateOp.itemCount -= updateOpObtainUpdateOp.itemCount;
                }
            }
            if (updateOp.positionStart > updateOp2.positionStart) {
                updateOp.positionStart -= updateOp2.itemCount;
            }
            if (updateOp.itemCount > updateOp2.positionStart) {
                updateOp.itemCount -= updateOp2.itemCount;
            }
        } else {
            if (updateOpObtainUpdateOp != null) {
                if (updateOp.positionStart >= updateOpObtainUpdateOp.positionStart) {
                    updateOp.positionStart -= updateOpObtainUpdateOp.itemCount;
                }
                if (updateOp.itemCount >= updateOpObtainUpdateOp.positionStart) {
                    updateOp.itemCount -= updateOpObtainUpdateOp.itemCount;
                }
            }
            if (updateOp.positionStart >= updateOp2.positionStart) {
                updateOp.positionStart -= updateOp2.itemCount;
            }
            if (updateOp.itemCount >= updateOp2.positionStart) {
                updateOp.itemCount -= updateOp2.itemCount;
            }
        }
        list.set(i, updateOp2);
        if (updateOp.positionStart != updateOp.itemCount) {
            list.set(i2, updateOp);
        } else {
            list.remove(i2);
        }
        if (updateOpObtainUpdateOp != null) {
            list.add(i, updateOpObtainUpdateOp);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x006c  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x00d7  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x00e4  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x00fc  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x010a  */
    /* JADX WARN: Removed duplicated region for block: B:26:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void swapMoveUpdate(java.util.List<android.support.v7.widget.AdapterHelper.UpdateOp> r7, int r8, android.support.v7.widget.AdapterHelper.UpdateOp r9, int r10, android.support.v7.widget.AdapterHelper.UpdateOp r11) {
        /*
            Method dump skipped, instructions count: 276
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.OpReorderer.swapMoveUpdate(java.util.List, int, android.support.v7.widget.AdapterHelper$UpdateOp, int, android.support.v7.widget.AdapterHelper$UpdateOp):void");
    }
}
