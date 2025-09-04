Coding_factory-master (Backend)

Στο backend έχει υλοποιηθεί η βασική δομή της εφαρμογής JobPortal, με χρήση Spring Boot και Maven. Η κεντρική κλάση JobportalApplication εκκινεί το σύστημα, ενώ τα entities όπως User, Applicant, Company, Job και Application αντιπροσωπεύουν τα βασικά αντικείμενα του domain.

Έχουν δημιουργηθεί repositories για κάθε entity, ώστε να επιτρέπεται η επικοινωνία με τη βάση δεδομένων μέσω Spring Data JPA. Οι queries είναι τύπου findByEmail, findByJob, findByApplicant, και καλύπτουν τις βασικές ανάγκες αναζήτησης και διαχείρισης αιτήσεων.

Στους controllers έχουν υλοποιηθεί endpoints για εγγραφή χρηστών, δημιουργία αγγελιών, υποβολή αιτήσεων, έγκριση και απόρριψη. Ο ApplicationController χειρίζεται την υποβολή αιτήσεων από υποψήφιους και την προβολή τους από εταιρείες, με έλεγχο ρόλων μέσω Spring Security.

Το σύστημα ασφαλείας έχει υλοποιηθεί με JWT authentication. Ο χρήστης λαμβάνει token κατά το login, και το token αυτό χρησιμοποιείται για την προστασία των endpoints. Ο ρόλος του χρήστη (APPLICANT, COMPANY) καθορίζει την πρόσβαση σε συγκεκριμένες λειτουργίες.

Τέλος, έχει ενσωματωθεί πλήρες CORS configuration για επικοινωνία με το frontend, καθώς και custom exception handling για περιπτώσεις όπως μη εξουσιοδοτημένη πρόσβαση ή αποτυχία εύρεσης εγγραφών. Το backend είναι έτοιμο να δεχτεί αιτήματα από το React frontend και να ανταποκριθεί με JSON.
