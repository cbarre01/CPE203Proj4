final class Viewport
{
   private int row;
   private int col;
   private int numRows;
   private int numCols;

   public Viewport(int numRows, int numCols)
   {
      this.numRows = numRows;
      this.numCols = numCols;
   }

   public void shift(int colIn, int rowIn)
   {
      col = colIn;
      row = rowIn;
   }

   public boolean contains(Point p)
   {
      return p.getY() >= row && p.getY() < row + numRows &&
              p.getX() >= col && p.getX() < col + numCols;
   }

   public Point viewportToWorld(int colIn, int rowIn)
   {
      return new Point(colIn + col, rowIn + row);
   }

   public Point worldToViewport(int colIn, int rowIn)
   {
      return new Point(colIn - col, rowIn - row);
   }

   public int getRow() {
      return row;
   }


   public int getCol() {
      return col;
   }


   public int getNumRows() {
      return numRows;
   }


   public int getNumCols() {
      return numCols;
   }

}
