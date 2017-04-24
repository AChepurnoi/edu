package lab;

class PointsProvider {

    final static Double[] FACE_POINTS;
    final static double FACE_BORDER_SIZE;

    final static Double[] TOP_TAIL_POINTS;
    final static Double[] BOTTOM_TAIL_POINTS;

    final static Double[] LEFT_BEAK_POINTS;
    final static Double[] RIGHT_BEAK_POINTS;
    final static double BEAK_BORDER_SIZE;

    final static Double[] LEFT_EYE_POINTS;
    final static Double[] RIGHT_EYE_POINTS;
    final static double EYE_BORDER_SIZE;

    final static Double[] LEFT_EYE_PUPIL_POINTS;
    final static Double[] RIGHT_EYE_PUPIL_POINTS;

    final static Double[] LEFT_EYEBROW_POINTS;
    final static Double[] RIGHT_EYEBROW_POINTS;

    final static Double[] CHIN_POINTS;


    static {
        FACE_POINTS = new Double[]{148.0, 58.0, 152.0, 49.0, 160.0, 41.0, 170.0, 43.0, 182.0, 47.0, 194.0, 51.0, 340.0, 183.0, 342.0, 193.0, 342.0, 201.0, 334.0, 211.0, 326.0, 215.0, 118.0, 263.0, 98.0, 261.0, 90.0, 253.0, 88.0, 245.0, 92.0, 223.0, 98.0, 197.0};
        FACE_BORDER_SIZE = 4.0;

        TOP_TAIL_POINTS = new Double[]{181.0, 43.0, 180.0, 40.0, 179.0, 27.0, 169.0, 13.0, 153.0, 5.0, 147.0, 5.0, 134.0, 6.0, 136.0, 8.0, 138.0, 14.0, 138.0, 20.0, 122.0, 18.0, 112.0, 18.0, 90.0, 22.0, 60.0, 34.0, 44.0, 44.0, 38.0, 56.0, 38.0, 58.0, 62.0, 48.0, 72.0, 46.0, 84.0, 42.0, 96.0, 46.0, 86.0, 52.0, 78.0, 62.0, 90.0, 60.0, 102.0, 58.0, 118.0, 54.0, 120.0, 64.0, 138.0, 54.0};
        BOTTOM_TAIL_POINTS = new Double[]{104.0, 184.0, 60.0, 178.0, 76.0, 190.0, 58.0, 188.0, 50.0, 188.0, 38.0, 188.0, 32.0, 192.0, 18.0, 196.0, 8.0, 206.0, 2.0, 218.0, 10.0, 214.0, 16.0, 212.0, 22.0, 210.0, 34.0, 206.0, 48.0, 204.0, 56.0, 204.0, 72.0, 202.0, 70.0, 210.0, 64.0, 214.0, 58.0, 220.0, 92.0, 222.0};

        LEFT_BEAK_POINTS = new Double[]{232.0, 200.0, 232.0, 194.0, 248.0, 168.0, 318.0, 182.0, 316.0, 186.0, 310.0, 188.0, 252.0, 192.0, 244.0, 194.0};
        RIGHT_BEAK_POINTS = new Double[]{229.0, 206.0, 247.0, 202.0, 267.0, 180.0, 301.0, 182.0, 269.0, 216.0};
        BEAK_BORDER_SIZE = 2.0;

        LEFT_EYE_POINTS = new Double[]{184.0, 169.0, 184.0, 182.0, 186.0, 191.0, 192.0, 197.0, 202.0, 198.0, 217.0, 196.0, 232.0, 184.0, 232.0, 175.0, 230.0, 169.0, 228.0, 166.0};
        RIGHT_EYE_POINTS = new Double[]{265.0, 164.0, 282.0, 177.0, 291.0, 178.0, 297.0, 175.0, 302.0, 169.0, 306.0, 157.0, 306.0, 153.0, 304.0, 145.0, 302.0, 144.0};
        EYE_BORDER_SIZE = 1.0;

        LEFT_EYE_PUPIL_POINTS = new Double[]{294.0, 160.0, 4.0};
        RIGHT_EYE_PUPIL_POINTS = new Double[]{219.0, 176.0, 5.0};

        LEFT_EYEBROW_POINTS = new Double[]{161.0, 152.0, 156.0, 151.0, 154.0, 152.0, 153.0, 155.0, 151.0, 158.0, 151.0, 161.0, 155.0, 165.0, 161.0, 168.0, 217.0, 171.0, 227.0, 170.0, 234.0, 165.0, 236.0, 160.0, 231.0, 158.0};
        RIGHT_EYEBROW_POINTS = new Double[]{263.0, 164.0, 262.0, 162.0, 260.0, 161.0, 258.0, 158.0, 259.0, 154.0, 261.0, 152.0, 311.0, 128.0, 315.0, 125.0, 319.0, 127.0, 322.0, 130.0, 322.0, 132.0, 321.0, 136.0, 317.0, 139.0};

        CHIN_POINTS = new Double[]{266.0, 228.0, 60.0, 30.0, 26.0, 177.0};
    }
}