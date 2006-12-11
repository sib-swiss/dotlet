#!/usr/bin/perl -w

# cmp2java.pl - converts a .cmp file into a Java source for an
# extension of the ScoreMatrix class.

# A .cmp-formatted matrix is only the upper triangle, but for speed
# gain we fill both halves of the matrix (thus no need to test if i >
# j); and we want to address elements directly by char value (minus
# 'A', C-style), so we need all rows and columns between A and Z
# inclusive, even if they correspond to no residue (like J for
# proteins).

use Getopt::Std;
getopts ("mn:t:");

$classname = $opt_n || "NewMatrix";
$template = $opt_t || "ScoreMatrix.tmpl";


# discard until we see a line with only capitals and space - first coordinate
while (($line = <>) !~ /([A-Z] +)+/) {}

# put the letters into an array
$line =~ s/[\.\s]//g;
@letters = split (//, $line);
$nletters = @letters;

foreach (@letters)
  {
    # skip empty lines
    do
      {
	$line = <>
      } until $line !~ /^\s*$/;

    # generate the array of scores
    $line =~ s/[A-Z]+//g;	# there normally should be only 1 letter...
    $line =~ s/^\s+//;		# slip leading whitespace
    @scores = split (/\s+/, $line);

    $nscores = @scores;

    # now unshift some unknown numbers to get the proper alignment
    for ($i = $nletters - $nscores; $i > 0; $i--)
      {
	unshift (@scores, 'NaN');
      }

    # fill the %scores_h hash of hashes, indexed by letters - filling the lower triangle
    for ($i = 0; $i <= $#scores; $i++)
      {
	if ($scores[ $i ] eq 'NaN')
	  {			# lower triangle - get value (already stored) from upper half 
	    $scores_h{ $_ }{ $letters[ $i ] } = $scores_h{ $letters[ $i ] }{ $_ };
	  }
	else
	  {			# upper triangle - get value (new) from @scores array
	    $scores_h{ $_ }{ $letters[ $i ] } = $scores[ $i ];
	  }
      }
  }

# Now, a more tricky part - insert gaps for unused letters (like J, U
# for prots), or many others for NAs

@alphabet = qw(A B C D E F G H I J K L M N O P Q R S T U V W X Y Z);

foreach $out (@alphabet)
  {
    foreach $in (@alphabet)
      {
	if (! exists $scores_h{ $out }{ $in })
	  {
	    $matrix{$out}{$in} = 0;
	  }
	else
	  {
	    $matrix{$out}{$in} = $scores_h{ $out }{ $in }
	  }
      }
  }



# Now generate the java code, unless the user wants only the matrix
# (-m)

unless ($opt_m)
  {
    print <<"END";
// $classname.java  

public class $classname extends ScoreMatrix
{
  $classname ()
    {
      int tmp [][] =
	{
END
   }      

     
# print it
$format = "\t\t{" . ("%3d," x 25) . "%3d},\n";   # 26: # of letters in the Roman alphabet 

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

