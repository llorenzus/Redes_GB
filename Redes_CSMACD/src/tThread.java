import java.util.Random;
import java.util.concurrent.atomic.*;

class tThread implements Runnable, Canal {
    String NumEstacao;
    Thread t;
    static int distancia;
    static int stat = 0;
    static int frame = 0;
    static int StatusCanal;
    int NumFrame;
    int MaxNumFrame;
    private AtomicBoolean CheckSucessoTransmissao;
    static int tfr=50; //cálculo do backofftime
    private int NumTentativas;
    tThread(String nomeThread, int MaxNumFrame) {
        NumEstacao = nomeThread;
        t = new Thread(this, NumEstacao);
        NumFrame = 1;
        this.MaxNumFrame = MaxNumFrame;
        CheckSucessoTransmissao = new AtomicBoolean();
        t.start();
    }
    public void run() {
        Random rand = new Random();
        while (!CheckSucessoTransmissao.get()) {
            NumTentativas++;
            while(NumFrame <= MaxNumFrame) {
                if (NumTentativas < 15) {
                    try {
                        if (StatusCanal == OCUPADO) {
                            System.out.println(NumEstacao + " canal ocupado, sensing");
                            try {
                                Thread.sleep(rand.nextInt(50)+1000);
                            }
                            catch (InterruptedException e) {
                                System.out.println(("tThread.RUN.InterruptedException"));
                            }
                        }
                        else {
                            System.out.println(NumEstacao + " tentando transmitir : " + NumFrame);

                            // Sucesso
                            if (StatusCanal == LIVRE && distancia == 0) {
                                String tName = Thread.currentThread().getName();
                                stat = (int) tName.charAt(tName.length()-1);
                                frame = this.NumFrame;

                                StatusCanal = OCUPADO;
                                for (; distancia < 9000000; distancia++)
                                    for(int i =0;i<1000;i++);

                                System.out.println(NumEstacao + " frame " + NumFrame + " sucesso");
                                CheckSucessoTransmissao.set(true);
                                NumFrame++;
                                distancia = 0;
                                StatusCanal = LIVRE;
                            }
                            //Colisão
                            else {
                                System.out.println("COLISÃO no frame " + NumFrame + " da estação " +
                                        NumEstacao + " e frame " + frame + " da estação " + stat);

                                System.out.println("Retransmitindo... " );
                                CheckSucessoTransmissao.set(false);
                                StatusCanal = LIVRE;
                                NumTentativas++;

                                try {
                                    int R = rand.nextInt((int) (Math.pow(2, NumTentativas - 1)));
                                    int BackOffTime = R * tfr;
                                    Thread.sleep(BackOffTime);

                                } catch (InterruptedException e) {
                                    System.out.println("tThread.RUN.InterruptedException");
                                }
                            }
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        System.out.println("tThread.RUN.InterruptedException");
                    }
                }
                else {
                    CheckSucessoTransmissao.set(true);
                    System.out.println("Muitas tentativas para frame " + NumFrame + "de " +
                            NumEstacao + ". Transmissão cancelada.");
                }
            }
        }
    }
}
