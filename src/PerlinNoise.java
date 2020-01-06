// This is based on code from https://www.youtube.com/watch?v=6-0UaeJBumA
public class PerlinNoise
{
    private int outputWidth;
    private int outputHeight;

    private double[][] noiseSeed;
    private double[][] perlinNoise;

    public PerlinNoise(int width, int height)
    {
        outputWidth = width;
        outputHeight = height;
        noiseSeed = new double[outputWidth][outputHeight];
        perlinNoise = new double[outputWidth][outputHeight];

        this.fillNoiseSeed();
        this.perlinNoise = this.calculatePerlinNoise2D(outputWidth, outputHeight, noiseSeed, 4);
    }


    // ==========================================
    //
    //          Initialization Functions
    //
    // ==========================================
    // Put random values between 0 and 1 in the seed array
    public void fillNoiseSeed()
    {
        for(int i = 0; i < outputWidth; i++)
        {
            for(int j = 0; j < outputHeight; j++)
            noiseSeed[i][j] = Math.random();
        }
    }



    // ==========================================
    //
    //             Perlin Functions
    //
    // ==========================================
    public double[] calculatePerlinNoise1D(int count, double[] seed, int numOctaves)
    {
        double[] output = new double[count];
        for(int i = 0; i < count; i++)
        {
            double noise = 0;
            double scale = 1;
            int pitch = count;
            double scaleSum = 0.0;
            for(int oct = 0; oct < numOctaves; oct++)
            {
                int sample1 = (i / pitch) * pitch;
                int sample2 = (sample1 + pitch) % count;

                double blend = (i - sample1) / (double)pitch;
                double sample = (1 - blend) * seed[sample1] + blend * seed[sample2];
                noise += scale*sample;

                pitch /= 2;

                scaleSum += scale;
                scale = scale / 2;
            }
            output[i] = noise / scaleSum;
        }
        return output;
    }

    public double[][] calculatePerlinNoise2D(int width, int height, double[][] seed, int numOctaves)
    {
        double[][] output = new double[width][height];
        for(int i = 0; i < width; i++)
        {
            for(int j = 0; j < height; j++)
            {
                double noise = 0;
                double scale = 1;
                int pitch = width;
                double scaleSum = 0.0;
                for(int oct = 0; oct < numOctaves; oct++)
                {
                    int sampleX1 = (i / pitch) * pitch;
                    int sampleY1 = (j / pitch) * pitch;

                    int sampleX2 = (sampleX1 + pitch) % width;
                    int sampleY2 = (sampleY1 + pitch) % width;

                    double blendX = (i - sampleX1) / (double)pitch;
                    double blendY = (j - sampleY1) / (double)pitch;
                    double sampleT = (1 - blendX) * seed[sampleY1][sampleX1] + blendX * seed[sampleY1][sampleX2];
                    double sampleB = (1 - blendX) * seed[sampleY2][sampleX1] + blendX * seed[sampleY2][sampleX2];

                    noise += (blendY * (sampleB - sampleT) + sampleT) * scale;

                    pitch /= 2;

                    scaleSum += scale;
                    scale = scale / 2;
                }
                output[i][j] = noise / scaleSum;
            }
        }
        return output;
    }


    // ==========================================
    //
    //                Getters
    //
    // ==========================================
    public double[][] getPerlinNoise()
    {
        return perlinNoise;
    }
}
