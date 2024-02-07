

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

class Kutu extends JButton{
    boolean acikmi = false;
    boolean bayraak = false;
    int satir;
    int sutun;
    int sayi=0;
    Icon icon;
    public Kutu(int x, int y) {
        satir=x;
        sutun=y;
        new JButton();
    }
}

class Mayinlar {

    int[][] mayinlar = new int[18][25];
    boolean[][] mayinMI = new boolean[18][25];

    Mayinlar() {

        int mayinSayisi = 40;
        Random random = new Random();

        for (int y = 0; y < 25; y++) {
            for (int x = 0; x < 18; x++) {
                mayinlar[x][y] = 0;

            }
        }

        while (mayinSayisi > 0) {

            int x = random.nextInt(18);
            int y = random.nextInt(25);

            while (!mayinMI[x][y]){
            mayinlar[x][y] = 1;
            mayinMI[x][y]=true;
            mayinSayisi--;
            }
        }
    }
}


public class Ekran extends Mayinlar{

    Kutu[][] kutu ;
    JFrame frame = new JFrame();
    Timer timer;
    int time=0;
    final int cBombasayisi = 40;
    int bombaSayisi=40;
    boolean patladi = false;

    JLabel zaman;
    JLabel kalanMayin;
    JLabel tilkicik;

    //todo  minesweeper/  rica ederim teşekküre gerek yok asjdbakjsdgaklfkh
    JLabel backgroundLabel = new JLabel(new ImageIcon("src/image/background.jpg"));
    ImageIcon tilki = new ImageIcon("src/image/tilki.png");
    ImageIcon bom = new ImageIcon("src/image/bom.png");
    ImageIcon bos = new ImageIcon("src/image/bos.png");
    ImageIcon bomflg = new ImageIcon("src/image/bomflg.png");
    ImageIcon bomba = new ImageIcon("src/image/bomba.png");
    ImageIcon patlayan = new ImageIcon("src/image/patlayan.png");
    ImageIcon bayrak = new ImageIcon("src/image/bayrak.png");
    ImageIcon cerceve = new ImageIcon("src/image/cerceve.png");

    Ekran(){ //ekrandaki görsellerin yerleştirilmesi
        kutu = new Kutu[18][25];

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        backgroundLabel.setBounds(0, 0, 2000, 2000);
        frame.setBounds(560,85,407,633);
        backgroundLabel.setBackground(new Color(198,233,251));
        backgroundLabel.setOpaque(true);
        frame.setLayout(null);

        //frame.setResizable(false);
        frame.setIconImage(bomflg.getImage());

        kalanMayin(bombaSayisi);
        tilki();
        timer();
        Buton();
        frame.add(backgroundLabel);
        frame.setVisible(true);

    }
    public void Buton(){

        for (int y = 0 ; y < 25 ; y++ ){  // 18*25 lik buton oluştur
            for (int x = 0 ; x < 18 ; x++ ){

                kutu[x][y] = new Kutu(x,y);
                kutu[x][y].setIcon(bos);

                kutu[x][y].setBounds(16+20*x,87+20*y,20,20);
                kutu[x][y].addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (isLeftMouseButton(e)){
                            //hangi butona tıklandığını bul
                            int satir = -800;
                            int sutun = -800;

                            for (int i = 0; i < 18; i++) {
                                for (int j = 0; j < 25; j++) {
                                    if (e.getSource() == kutu[i][j]) {
                                        satir = i;
                                        sutun = j;
                                        break;
                                    }
                                }
                            }
                            if (!kutu[satir][sutun].acikmi){
                            if ( !(satir == -800) && !(sutun == -800) ){ // tıklanan kutuyu gösteriyor
                                kutuyugoster(satir,sutun);
                            }

                            } else if (kutu[satir][sutun].acikmi) {
                                if ( !(satir == -800) && !(sutun == -800) ){
                                    if (!patladi)   {
                                         hizliAcma(satir,sutun);
                                    }
                                }
                            }
                        }

                        // todo Sağ geçiş

                        else if (isRightMouseButton(e)) {
                            if (!patladi) {
                                int satir = -800;
                                int sutun = -800;
                                for (int i = 0; i < 18; i++) {
                                    for (int j = 0; j < 25; j++) {
                                        if (e.getSource() == kutu[i][j]) {
                                            satir = i;
                                            sutun = j;
                                            break;
                                        }
                                    }
                                }

                                if (!kutu[satir][sutun].acikmi) {

                                    if (!kutu[satir][sutun].bayraak && bombaSayisi > 0) {

                                        bombaSayisi--;
                                        String string = String.valueOf(bombaSayisi);
                                        kalanMayin.setText(string);

                                        kutu[satir][sutun].setIcon(bayrak);

                                        kutu[satir][sutun].bayraak = true;
                                        bitisKontrol();
                                    } else if (kutu[satir][sutun].bayraak) {

                                        bombaSayisi++;
                                        String string = String.valueOf(bombaSayisi);
                                        kalanMayin.setText(string);

                                        if (!(satir == -800) && !(sutun == -800)) {
                                            kutu[satir][sutun].setIcon(bos);
                                        }
                                        kutu[satir][sutun].bayraak = false;

                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e){}

                    @Override
                    public void mouseReleased(MouseEvent e) {}

                    @Override
                    public void mouseEntered(MouseEvent e) {}

                    @Override
                    public void mouseExited(MouseEvent e) {}
                });

                sayibul(x,y);

                kutu[x][y].setVisible(true);

                frame.add(kutu[x][y]);
            }
        }

    }

    public void kutuyugoster(int satir, int sutun) {  //ya mayın var ya boş kutu ya da sayı var
        if (!kutu[satir][sutun].bayraak && !patladi){
            if (satir < 0 || sutun < 0 || satir >= 18 || sutun >= 25 || kutu[satir][sutun].acikmi) {//bakılan kutu sınır dışındaysa döngüye girmesin
                return;
            }

            kutu[satir][sutun].acikmi=true;

            if (mayinlar[satir][sutun] == 1) {
                kutu[satir][sutun].setIcon(patlayan);
                kutu[satir][sutun].icon=patlayan;
                patladinAslanim();

            } else if (kutu[satir][sutun].sayi==0) {

                kutu[satir][sutun].setIcon(kutu[satir][sutun].icon);

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {

                        if (gecerliKonum(satir + i, sutun + j)) {
                            kutuyugoster(satir + i, sutun + j);
                        }
                    }
                }

            }else{ //sayi
                kutu[satir][sutun].setIcon(kutu[satir][sutun].icon);
            }
        }
    }

    private void hizliAcma(int satir, int sutun){

        int hizliAcma=0;
        for (int j = -1; j <= 1; j++) {
            for (int i = -1; i <= 1; i++) {

                if (gecerliKonum(satir + i, sutun + j)) {

                    if (kutu[satir + i][sutun + j].bayraak) {
                      hizliAcma++;
                    }
                }
            }
        }
        for (int j = -1; j <= 1; j++) {
            for (int i = -1; i <= 1; i++) {
                if (gecerliKonum(satir + i, sutun + j)) {

                    if ((hizliAcma==kutu[satir][sutun].sayi)){

                        if (mayinlar[satir+i][sutun+j]==1 && !kutu[satir+i][sutun+j].bayraak){
                            patladinAslanim();
                        }
                        else if (!kutu[satir+i][sutun+j].bayraak){


                                kutuyugoster(satir+i,sutun+j);



                        }
                    }
                }
            }
        }
    }

    public void patladinAslanim() {
        patladi=true;
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 25; j++) {
                if (mayinlar[i][j] == 1 ) {


                    if (!kutu[i][j].bayraak) {
                        if ( kutu[i][j].icon!=patlayan){


                        kutu[i][j].setIcon(bom);}

                    } else if (kutu[i][j].bayraak) {

                        kutu[i][j].setIcon(bomflg);

                    }
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Patladın aslanım tekrar dene");
        timer.stop();
    }

    private boolean gecerliKonum(int satir, int sutun) {
        return satir >= 0 && satir < 18 && sutun >= 0 && sutun < 25;
    }
    public void sayibul(int x, int y) {
        if (mayinlar[x][y] != 1) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int yeniX = x + i;
                    int yeniY = y + j;

                    if (gecerliKonum(yeniX, yeniY) && mayinlar[yeniX][yeniY] == 1) {
                        kutu[x][y].sayi += 1;
                    }
                }
            }
        }
        for (int i=0; i<9; i++){ //iconbulla birleşiyormuş harbiden
            if (kutu[x][y].sayi==i){
                kutu[x][y].icon = new ImageIcon("src/image/"+i+".png");
            }
        }
    }

    public void timer(){
        zaman = new JLabel();
        zaman.setBounds(298,20,70,60);
        zaman.setFont(new Font("Arial", Font.BOLD, 18));

        zaman.setIcon(cerceve);

        zaman.setVisible(true);
        frame.add(zaman);
        zaman.setText("00:00");
        zaman.setHorizontalTextPosition(JLabel.CENTER);
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                time++;
                int minutes = time / 60;
                int seconds = time % 60;

                String timeString = String.format("%02d:%02d", minutes, seconds);
                zaman.setText(timeString);
            }
        });
        timer.start();
    }

    void tilki(){
        tilkicik = new JLabel();
        tilkicik.setBounds(154,6,85,85);

        tilkicik.setIcon(tilki);
        tilkicik.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { //tilkiye basınca yeni ekran
                frame.dispose();
                new Ekran();
            }
        });
        frame.add(tilkicik);
    }

    void bitisKontrol(){
        int bitis = 0;

        for (int y = 0 ; y <25 ; y++){
            for (int x = 0 ; x < 18 ; x++){

                if (mayinMI[x][y]){
                    if (kutu[x][y].bayraak){
                        bitis++;
                    }
                }
            }
        }
        if (bitis==cBombasayisi) {
            timer.stop();
            int minutes = time / 60;
            int seconds = time % 60;
            String timeString = String.format("%02d:%02d", minutes, seconds);
            JOptionPane.showMessageDialog(null, "Helal aslanım süren : " +timeString);
            patladi=true;
        }

    }

    void kalanMayin(int bombaSayisi){
        kalanMayin = new JLabel();

        kalanMayin.setBounds(14,29,70,60);
        kalanMayin.setFont(new Font("Arial", Font.BOLD, 17));
        kalanMayin.setForeground(Color.RED);

        String string = String.valueOf(bombaSayisi);
        kalanMayin.setText(string);
        kalanMayin.setIcon(bomba);

        kalanMayin.setHorizontalTextPosition(JLabel.CENTER);
        frame.add(kalanMayin);

    }

    public static void main(String[] args) {
        Ekran ekran = new Ekran();
    }
}