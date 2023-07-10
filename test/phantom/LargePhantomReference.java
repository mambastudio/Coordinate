/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phantom;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

/**
 *
 * @author user
 * @param <LargeObject>
 */
public class LargePhantomReference<LargeObject> extends PhantomReference<LargeObject>  {
    private final ReferenceQueue<LargeObject> referenceQueue;
    public LargePhantomReference(LargeObject obj, ReferenceQueue<LargeObject> queue) {

       super(obj, queue);
       this.referenceQueue = queue;
       
       Thread thread = new Thread(this::run);
       thread.start();
    }
    
    public void cleanup() {
       System.out.println("Cleaning up a phantom reference! Removing an object from memory!");
       clear();
    }
        
    private void run() {

       System.out.println("The thread monitoring the queue has started!");
       Reference ref = null;

       // Wait until the references appear in the referenceQueue
       while ((ref = referenceQueue.poll()) == null) {

           try {
               Thread.sleep(50);
           }

           catch (InterruptedException e) {
               throw new RuntimeException("Thread in phantom object was interrupted!");
           }
       }

       // As soon as a phantom reference appears in the referenceQueue, clean it up
       ((LargePhantomReference) ref).cleanup();
   }
}
