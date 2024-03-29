/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phantom;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class CleanerObject {
    private static final CleanerObject INSTANCE = new CleanerObject();

    public static CleanerObject getCleanerObject() {
        return INSTANCE;
    }

    private final ReferenceQueue<Object> referenceQueue;
    private final Thread cleanerThread;
    private CleanerRef firstCleanable;

    private CleanerObject() {
        referenceQueue = new ReferenceQueue<>();
        cleanerThread = new Thread() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Reference<? extends Object> ref = referenceQueue.remove();
                        if(ref instanceof CleanerRef) {
                            ((CleanerRef) ref).clean();
                        }
                    } catch (InterruptedException ex) {
                        // Can be raised on shutdown. If anyone else messes with
                        // our reference queue, well, there is no way to separate
                        // the two cases.
                        // https://groups.google.com/g/jna-users/c/j0fw96PlOpM/m/vbwNIb2pBQAJ
                        break;
                    } catch (Exception ex) {
                        Logger.getLogger(CleanerObject.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        cleanerThread.setName("JNA Cleaner");
        cleanerThread.setDaemon(true);
        cleanerThread.start();
    }

    public synchronized Cleanable register(Object obj, Runnable cleanupTask) {
        // The important side effect is the PhantomReference, that is yielded
        // after the referent is GCed
        return add(new CleanerRef(this, obj, referenceQueue, cleanupTask));
    }

    private synchronized CleanerRef add(CleanerRef ref) {
        if(firstCleanable == null) {
            firstCleanable = ref;
        } else {
            ref.setNext(firstCleanable);
            firstCleanable.setPrevious(ref);
            firstCleanable = ref;
        }
        return ref;
    }

    private synchronized boolean remove(CleanerRef ref) {
        boolean inChain = false;
        if(ref == firstCleanable) {
            firstCleanable = ref.getNext();
            inChain = true;
        }
        if(ref.getPrevious() != null) {
            ref.getPrevious().setNext(ref.getNext());
        }
        if(ref.getNext() != null) {
            ref.getNext().setPrevious(ref.getPrevious());
        }
        if(ref.getPrevious() != null || ref.getNext() != null) {
            inChain = true;
        }
        ref.setNext(null);
        ref.setPrevious(null);
        return inChain;
    }

    private static class CleanerRef extends PhantomReference<Object> implements Cleanable {
        private final CleanerObject cleaner;
        private final Runnable cleanupTask;
        private CleanerRef previous;
        private CleanerRef next;

        public CleanerRef(CleanerObject cleaner, Object referent, ReferenceQueue<? super Object> q, Runnable cleanupTask) {
            super(referent, q);
            this.cleaner = cleaner;
            this.cleanupTask = cleanupTask;
        }

        @Override
        public void clean() {
            if(cleaner.remove(this)) {
                cleanupTask.run();
            }
        }

        CleanerRef getPrevious() {
            return previous;
        }

        void setPrevious(CleanerRef previous) {
            this.previous = previous;
        }

        CleanerRef getNext() {
            return next;
        }

        void setNext(CleanerRef next) {
            this.next = next;
        }
    }

    public static interface Cleanable {
        public void clean();
    }
}
