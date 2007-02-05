#!/usr/bin/perl -w

# mat2java - converts a .mat matrix into a form suitable for use in a
# ScoreMatrix subclass.

use Getopt::Std;
getopts ("mn:t:");

$classname = $opt_n || "NewMatrix";
$template = $opt_t || "ComparisonMatrix.tmpl";

################################################################
# Read the .mat file and build the letter-addressed hashes of values

# skip the comments
while (($line = <>) =~ /^\#/) {} 

# store the letters (order seems weird...)
$line =~ s/\s+//g;
@letters = split (//, $line);
$nletters = @letters;

# now read each line, building an intermediate hash with only the
# letters featured in the .mat file

foreach (@letters)		# read 1 line for each letter
  {				# assuming they're in the same order
    $line = <>;
    
    @scores = split (/\s+/, $line);
    warn ("horizontal and vertical orders differ!") if shift (@scores) ne $_;

    # print "scores: " . join (', ', @scores) . "\n";
    
    # now fill hash: $_ is the vertical letter, $letters[ $i ] the horizontal
    for ($i = 0; $i < $nletters; $i++)
      {
	$scores_h{ $_ }{ $letters[ $i ] } = $scores[ $i ];
      }
  }


# time to fill the real hash (`matrix`), with all letters in the
# alphabet

@alphabet = qw (A B C D E F G H I J K L M N O P Q R S T U V W X Y Z *);

foreach $out (@alphabet)
  {
    foreach $in (@alphabet)
      {
	if (! exists $scores_h{ $out }{ $in })
	  {
	    # at least 1 unknown letter
	    if ($out eq $in)
	      {
		# when it's the same unkown letter
		$matrix{ $out }{ $in } = $scores_h{ '*' }{ '*' };
	      }
	    else
	      {
		# two different unkown letters
		$matrix{ $out }{ $in } = $scores_h{ '*' }{ 'A' };
	      }
	  }
	else
	  {
	    # ok, both letters are known
	    $matrix{ $out }{ $in } = $scores_h{ $out }{ $in };
	  }
      }
  }


# Now generate the java code, unless the user wants only the matrix
# (-m)

unless ($opt_m)
  {
    print <<"END";
// $classname.java  

public class $classname extends ComparisonMatrix
{
  $classname ()
    {
      int tmp [][] =
	{
END
   }      

     
# print it
$format = "\t\t{" . ("%3d," x 26) . "%3d},\n";   # 26: # of letters in the Roman alphabet 

foreach $out (@alphabet)
  {
    my @vals = ();
    
    foreach $in (@alphabet)
      {
	push (@vals, $matrix{$out}{$in});
      }

    printf ($format, @vals);
  }

unless ($opt_m)
{     
  open ('TMPL', "< $template") or
    warn ("Could not open $template for reading ($!), printing matrix only.\n");

  foreach (<TMPL>) { print; }
}

