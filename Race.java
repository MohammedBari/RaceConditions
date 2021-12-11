/*

Mohammed Bari

 */
import java.lang.Thread;
import java.util.concurrent.Semaphore;
import java.util.Random;

public class Race {

    static int programLimit = 25;
    static boolean token = false;
    static Random rando = new Random();
    static int counter = 0;
    static int nextin = 0;
    static int nextout = 0;
    static int [] b = new int[15];
    static Semaphore e = new Semaphore(b.length);
    static Semaphore f = new Semaphore(0);

    public static void main(String[] args) throws InterruptedException {
        //Thread for producer functions
        Thread pThread = new Thread((new Runnable(){
            @Override
            public void run() {
                try {
                    while(!token){
                        int k1 = rando.nextInt(15/2)+1;

                        for(int i = 0; i < k1-1; i++){
                            e.acquire();
                            b[(nextin + i) % b.length] = 1; //buffer[next_in + k1 mod n] = 1
                            f.release();
                        }
                        nextin = (nextin + k1) % b.length; // next_in = next_in + k1 mod n
                        System.out.println("PRODUCER: Next In = " + nextin);
                        programLimit--;

                        if(programLimit <= 0){
                            System.out.println("PRODUCER: operations complete");
                            token = true;
                        }
                        Thread.sleep((int) (Math.random()*50));
                    }                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }));

        //Thread for consumer functions
        Thread cThread = new Thread((new Runnable(){
            @Override
            public void run() {
                try{
                    while (!token){
                        Thread.sleep((int) (Math.random()*50));
                        int k2 = rando.nextInt(15/2) + 1;
                        int data;

                        for(int i = 0; i < k2-1; i++){
                            f.acquire();
                            data= b[(nextin + i) % b.length]; //data = buffer[next_out + k2 mod n]
                            if(data >= 1){
                                System.out.println("Race condition here.");
                                counter++;
                                f.release();
                            }
                        }
                        nextout = (nextout + k2) % b.length;
                        System.out.println("CONSUMER: Next out = " + nextout);
                        programLimit--;

                        if(programLimit <= 0) {
                            System.out.println(counter + ": Race Conditions\n");
                            token = true;
                        }
                        Thread.sleep((int) (Math.random()*50));
                    }                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }));

        pThread.start();
        cThread.start();
        pThread.join();
        cThread.join();
    }

}

