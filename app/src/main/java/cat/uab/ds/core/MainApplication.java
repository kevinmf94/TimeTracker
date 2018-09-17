package cat.uab.ds.core;

public class MainApplication {

    public static void main(String args[]){
        System.out.println("Hello world!");

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello");
            }
        }).start();
    }

}
