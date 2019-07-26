## Write regex to extract emails and phone numbers from a set of emails.

The set of emails is in the folder 'emails'.

The formats which your email regex should match are:
jurafsky@stanford.edu
jurafsky(at)cs.stanford.edu
jurafsky at csli dot stanford dot edu
 <script type="text/javascript">obfuscate('stanford.edu','jurafsky')</script> 

 And return the canonical form:
 jurafsky@stanford.edu


 Your phone number regex shoud match:
650-723-0293
TEL +1-650-723-0293
Phone:  (650) 723-0293
Tel (+1): 650-723-0293
<a href="contact.html">TEL</a> +1&thinsp;650&thinsp;723&thinsp;0293

 And return the canonical form:
650-723-0293

All phone numbers have North American prefix.


There is a gold standard file which you can evaluate your regex against. To do that, just execute the file email_regex.py. It will show you the amount of false negatives, false positives, true negatives and true positives your code outputs.
