/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinate.utility;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 * 
 * Cleaner like class for post garbage collection calls (like freeing native memory)
 * 
 * Shamelessly borrowed from JNA
 * 
 */
public class Sweeper {
    private static final Sweeper sweeper = new Sweeper();    
    
    public static Sweeper getSweeper() {
        return sweeper;
    }
    
    private final ReferenceQueue<Object> referenceQueue;
    private final Thread thread;
    private SweeperRef firstSweeper;
    
    private Sweeper() {
        referenceQueue = new ReferenceQueue<>();
        thread = new Thread(()->{
            while(true) {
                    try {
                        Reference<? extends Object> ref = referenceQueue.remove();
                        if(ref instanceof SweeperRef) {
                            ((SweeperRef) ref).clean();
                        }
                    } catch (InterruptedException ex) {
                        // Can be raised on shutdown. If anyone else messes with
                        // our reference queue, well, there is no way to separate
                        // the two cases.
                        // https://groups.google.com/g/jna-users/c/j0fw96PlOpM/m/vbwNIb2pBQAJ
                        break;
                    } catch (Exception ex) {
                        Logger.getLogger(Sweeper.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
        });     
        
        thread.setName("Sweeper cleaner");
        thread.setDaemon(true);
        thread.start();
    }
    
    public synchronized Sweepable register(Object obj, Runnable cleanupTask) {
        // The important side effect is the PhantomReference, that is yielded
        // after the referent is GCed
        return add(new SweeperRef(this, obj, referenceQueue, cleanupTask));
    }

    private synchronized SweeperRef add(SweeperRef ref) {
        if(firstSweeper == null) {
            firstSweeper = ref;
        } else {
            ref.setNext(firstSweeper);
            firstSweeper.setPrevious(ref);
            firstSweeper = ref;
        }
        return ref;
    }
    
    private synchronized boolean remove(SweeperRef ref) {
        boolean inChain = false;
        if(ref == firstSweeper) {
            firstSweeper = ref.getNext();
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
    
    private static class SweeperRef extends PhantomReference<Object> implements Sweepable {
        private final Sweeper sweeper;
        private final Runnable sweeperTask;
        private SweeperRef previous;
        private SweeperRef next;
        
        public SweeperRef(Sweeper sweeper, Object referent, ReferenceQueue<? super Object> q, Runnable sweeperTask) {
            super(referent, q);
            this.sweeper = sweeper;
            this.sweeperTask = sweeperTask;
        }

        @Override
        public void clean() {
            if(sweeper.remove(this)) {
                sweeperTask.run();
            }
        }       
        
        SweeperRef getPrevious() {
            return previous;
        }

        void setPrevious(SweeperRef previous) {
            this.previous = previous;
        }

        SweeperRef getNext() {
            return next;
        }

        void setNext(SweeperRef next) {
            this.next = next;
        }
    }
    
    public static interface Sweepable {
        public void clean();
    }
}
