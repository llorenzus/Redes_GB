import java.util.Scanner;

class CSMACD implements Canal {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        tThread.StatusCanal = LIVRE;
        System.out.println("Digite o número de estações: ");
        int quantEstacoes = sc.nextInt();
        tThread tThreadsList[] = new tThread[quantEstacoes+1];
        int FrameArray[] = new int[quantEstacoes+1];
        for(int i = 1;i<=quantEstacoes;i++)
        {
            System.out.println("Digite a quantidade a ser transmitida pela estação: " + i);
            FrameArray[i] = sc.nextInt();
        }
        for(int i = 1;i<=quantEstacoes;i++)
            tThreadsList[i] = new tThread("Estacao "+ Integer.toString(i),FrameArray[i]);
        try {
            for(int i=1;i<=quantEstacoes;i++)
                tThreadsList[i].t.join();
        }
        catch (InterruptedException e) {
            System.out.println("CSMACD.main.InterruptedException");
        }
        System.out.println("Transmissão concluída.");
    }
}
