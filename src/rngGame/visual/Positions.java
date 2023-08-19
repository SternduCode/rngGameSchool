package rngGame.visual;

public enum Positions {
	
	Topleft			(0			,		  0),
	Topmiddle		(960/2		,		  0),
	Topright		(960		,		  0),
	Middleleft		(0			,	 528/2),
	Mcenter			(960/2		,	 528/2),
	Middleright 	(960		,	 528/2),
	Bottomleft		(0			,		528),
	Bottommiddle	(960/2		,		528),
	Bottomright 	(960		,		528);
	
	final int x, y;
	
	Positions(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
}
