import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
class Kutu extends JButton{
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

            mayinlar[x][y] = 1;

            mayinSayisi--;
        }
    }
}


public class Ekran extends Mayinlar{

    //JButton[][] buton ;
    Kutu[][] kutu ;
    JFrame frame = new JFrame();
    Timer timer;
    int time=0;
    JLabel zaman;
    JLabel kalanMayin;
    JLabel tilkicik;
    ImageIcon tilki = new ImageIcon("src/image/tilki.png");
    ImageIcon bom = new ImageIcon("src/image/bom.png");
    ImageIcon bomba = new ImageIcon("src/image/bomba.png");

    Ekran(){ //ekrandaki görsellerin yerleştirilmesi
        kutu = new Kutu[18][25];
        //buton = new JButton[18][25];

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setBounds(0,0,407,633);

        frame.setLayout(null);

        kalanMayin();
        tilki();
        timer();
        Buton();
        frame.setVisible(true);

    }
    public void Buton(){

        for (int y = 0 ; y < 25 ; y++ ){  // 18*25 lik buton oluştur
            for (int x = 0 ; x < 18 ; x++ ){


                kutu[x][y] = new Kutu(x,y);

                kutu[x][y].setBounds(16+20*x,87+20*y,20,20);
                kutu[x][y].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) { //hangi butona tıklandığını bul
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
                        if ( !(satir == -800) && !(sutun == -800) ){ // tıklanan kutuyu gösterecek inş
                            kutuyugoster(satir,sutun);
                        }
                    }
                });
                sayibul(x,y);
                iconbul(x,y);
                kutuyugoster(x,y); // şimdilik bombayı falab direkt göstersin

                kutu[x][y].setVisible(true);

                frame.add(kutu[x][y]);
            }
        }

    }

    public void kutuyugoster(int satir, int sutun) {  //ya mayın var ya boş kutu ya da sayı var

        if (mayinlar[satir][sutun] == 1) {
            kutu[satir][sutun].setIcon(bom);
            //oyun bitecek patlama efekti bum
        } else if (kutu[satir][sutun].sayi==0) {
            // kutu[satir][sutun].setIcon();
            //todo etrafını nasıl açcaz :)
        }else
            kutu[satir][sutun].setIcon(kutu[satir][sutun].icon);
    }

    public void sayibul(int x, int y) {

        if (mayinlar[x][y]!=1 ){
            //todo kenardaki kutularda hata çıkıyo diye 17 24 yaptım sen bakarsın
            if ((x-1 >= 0 && x-1 < 18 && y-1 >= 0 && y-1 < 25)  &&  (mayinlar[x - 1][y - 1] == 1))
                kutu[x][y].sayi += 1;

            if (( x < 18 && y-1 >= 0 && y-1 < 25)  &&  (mayinlar[x][y - 1] == 1))
                kutu[x][y].sayi += 1;

            if (( x+1 < 18 && y-1 >= 0 && y-1 < 25)  &&  (mayinlar[x + 1][y - 1] == 1))
                kutu[x][y].sayi += 1;

            if ((x-1 >= 0 && x-1 < 18 && y < 25)  &&  (mayinlar[x - 1][y] == 1))
                kutu[x][y].sayi += 1;

            if (( x +1 < 18 && y < 25)  &&  (mayinlar[x + 1][y] == 1))
                kutu[x][y].sayi += 1;
//:)
            if ((x-1 >= 0 && x-1 < 18 &&  y+1 < 25)  &&  (mayinlar[x - 1][y + 1] == 1))
                kutu[x][y].sayi += 1;

            if ((x < 18 && y+1 < 25)  &&  (mayinlar[x][y + 1] == 1))
                kutu[x][y].sayi += 1;

            if ((x+1 < 18 && y+1 < 25)  &&  (mayinlar[x + 1][y + 1] == 1))
                kutu[x][y].sayi += 1;
        }
    }

    public void iconbul(int satir, int sutun) {
        for (int i=1; i<9; i++){
            if (kutu[satir][sutun].sayi==i){
                ImageIcon sayi = new ImageIcon("src/image/"+i+".png");

                kutu[satir][sutun].icon = sayi;
                //kutu[satir][sutun].setIcon(sayi); //bu silinecek kutugöstere yazcaz
            }
        }
    }

    public void timer(){
        zaman = new JLabel();
        zaman.setBounds(310,16,70,60);
        zaman.setFont(new Font("Arial", Font.BOLD, 20));

        zaman.setVisible(true);
        frame.add(zaman);
        zaman.setText("00:00");

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
        tilkicik.setBounds(166,10,80,80);

        tilkicik.setIcon(tilki);
        frame.add(tilkicik);
    }

    void kalanMayin(){
        kalanMayin = new JLabel();

        kalanMayin.setBounds(30,20,70,60);
        kalanMayin.setFont(new Font("Arial", Font.BOLD, 20));
        kalanMayin.setForeground(Color.white);
        kalanMayin.setText("40");
        kalanMayin.setIcon(bomba);

        kalanMayin.setHorizontalTextPosition(JLabel.CENTER);
        frame.add(kalanMayin);

    }

    public static void main(String[] args) {
        Ekran ekran = new Ekran();
    }
}