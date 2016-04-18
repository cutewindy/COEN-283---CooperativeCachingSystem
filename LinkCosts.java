public class LinkCosts {
	private int N;
	private double[][] matrix;   // N-by-N array
	
	public LinkCosts(int N){
		this.N = N;
		this.matrix = this.generateLinkCosts();
	};
	
	private static double generateFixLinkCosts(int i, int j) {
		// Generate LinkCosts in random
//		matrix = new double[N][N];
//        for (int i = 0; i < this.N; i++)
//            for (int j = 0; j < this.N; j++)
//            	matrix[i][j] = -1;
//        
//        for (int i = 0; i < this.N; i++)
//            for (int j = 0; j < this.N; j++)
//            	if (i == j) {
//            		matrix[i][j] = 0; // self <-> self
//            	}
//            	else if (matrix[i][j] == -1) {
//            		matrix[i][j] = Math.random(); // as <-> as
//            		matrix[j][i] = matrix[i][j];  // mirror
//            	}
        
        // Generate LinkCosts in fix 10 * 10
//       double[][] fixMatrix = {
//    		   {0.000000, 0.167080, 0.705776, 0.790552, 0.345314, 0.486379, 0.079461, 0.759658, 0.084095, 0.990722},
//               {0.167080, 0.000000, 0.231232, 0.406228, 0.262223, 0.508135, 0.897119, 0.750931, 0.566916, 0.025404},
//               {0.705776, 0.231232, 0.000000, 0.199860, 0.546836, 0.905978, 0.385718, 0.541213, 0.464620, 0.690166},
//               {0.790552, 0.406228, 0.199860, 0.000000, 0.986181, 0.171485, 0.080172, 0.469927, 0.853900, 0.652138},
//               {0.345314, 0.262223, 0.546836, 0.986181, 0.000000, 0.907035, 0.088140, 0.902191, 0.326735, 0.654635},
//               {0.486379, 0.508135, 0.905978, 0.171485, 0.907035, 0.000000, 0.550568, 0.872544, 0.346156, 0.612964},
//               {0.079461, 0.897119, 0.385718, 0.080172, 0.088140, 0.550568, 0.000000, 0.886997, 0.453347, 0.755921},
//               {0.759658, 0.750931, 0.541213, 0.469927, 0.902191, 0.872544, 0.886997, 0.000000, 0.565353, 0.378576},
//               {0.084095, 0.566916, 0.464620, 0.853900, 0.326735, 0.346156, 0.453347, 0.565353, 0.000000, 0.332966},
//               {0.990722, 0.025404, 0.690166, 0.652138, 0.654635, 0.612964, 0.755921, 0.378576, 0.332966, 0.000000}};
		
		// generate LinkCosts in fix 15 * 15
//        double[][] fixMatrix = {
//        		{0.000000, 0.392565, 0.479975, 0.861230, 0.796738, 0.300257, 0.593838, 0.186806, 0.503812, 0.189846, 0.539045, 0.796618, 0.300766, 0.063594, 0.576955}, 
//        		{0.392565, 0.000000, 0.203859, 0.101133, 0.742440, 0.467569, 0.881369, 0.100351, 0.252609, 0.465850, 0.650301, 0.252921, 0.640716, 0.265931, 0.181136}, 
//        		{0.479975, 0.203859, 0.000000, 0.166095, 0.835318, 0.731132, 0.666895, 0.896935, 0.001973, 0.803360, 0.573119, 0.335887, 0.949200, 0.072404, 0.995164}, 
//        		{0.861230, 0.101133, 0.166095, 0.000000, 0.087538, 0.534343, 0.103785, 0.560588, 0.018020, 0.691210, 0.773236, 0.002881, 0.928575, 0.655430, 0.536547}, 
//        		{0.796738, 0.742440, 0.835318, 0.087538, 0.000000, 0.502508, 0.136056, 0.198850, 0.775451, 0.642237, 0.093711, 0.200867, 0.103347, 0.355913, 0.569363}, 
//        		{0.300257, 0.467569, 0.731132, 0.534343, 0.502508, 0.000000, 0.682483, 0.789110, 0.563346, 0.313143, 0.507920, 0.262874, 0.278702, 0.593232, 0.319034}, 
//        		{0.593838, 0.881369, 0.666895, 0.103785, 0.136056, 0.682483, 0.000000, 0.004611, 0.855738, 0.455589, 0.909326, 0.984476, 0.690499, 0.072458, 0.167237}, 
//        		{0.186806, 0.100351, 0.896935, 0.560588, 0.198850, 0.789110, 0.004611, 0.000000, 0.223303, 0.140501, 0.084450, 0.922486, 0.972319, 0.639237, 0.412319}, 
//        		{0.503812, 0.252609, 0.001973, 0.018020, 0.775451, 0.563346, 0.855738, 0.223303, 0.000000, 0.514579, 0.473028, 0.985725, 0.617073, 0.669205, 0.251414}, 
//        		{0.189846, 0.465850, 0.803360, 0.691210, 0.642237, 0.313143, 0.455589, 0.140501, 0.514579, 0.000000, 0.571128, 0.520843, 0.316108, 0.424992, 0.867904}, 
//        		{0.539045, 0.650301, 0.573119, 0.773236, 0.093711, 0.507920, 0.909326, 0.084450, 0.473028, 0.571128, 0.000000, 0.466002, 0.311102, 0.556933, 0.851666}, 
//        		{0.796618, 0.252921, 0.335887, 0.002881, 0.200867, 0.262874, 0.984476, 0.922486, 0.985725, 0.520843, 0.466002, 0.000000, 0.286269, 0.635255, 0.178374}, 
//        		{0.300766, 0.640716, 0.949200, 0.928575, 0.103347, 0.278702, 0.690499, 0.972319, 0.617073, 0.316108, 0.311102, 0.286269, 0.000000, 0.151959, 0.791126}, 
//        		{0.063594, 0.265931, 0.072404, 0.655430, 0.355913, 0.593232, 0.072458, 0.639237, 0.669205, 0.424992, 0.556933, 0.635255, 0.151959, 0.000000, 0.535881}, 
//        		{0.576955, 0.181136, 0.995164, 0.536547, 0.569363, 0.319034, 0.167237, 0.412319, 0.251414, 0.867904, 0.851666, 0.178374, 0.791126, 0.535881, 0.000000}};
        
		
		double[][] fixMatrix = new double[15][15];
		for (int k = 0; k < 15; k ++) {
			for (int l = 0; l < 15; l++) {
				fixMatrix[k][l] = 0.3;
			}
 		}
		
        return fixMatrix[i][j];
	}
	
	public double[][] generateLinkCosts() {
		matrix = new double[N][N];
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				matrix[i][j] = generateFixLinkCosts(i, j);
			}
		}
		return matrix;
	}
	
    // Print matrix to standard output	
    public void showMatrix() {
    	System.out.printf("N: %s\n", this.N);
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) 
                System.out.printf("%9.6f ", this.matrix[i][j]);
            System.out.println();
        }
    }

}
