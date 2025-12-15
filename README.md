# sla-analysis-and-monitoring
Code from paper "Cloud Certification Process Validation using Formal Methods" (ICSOC 2017)

Here's the code from the paper ["Krotsiani, M., Kloukinas, C. & Spanoudakis, G. (2017). Cloud Certification Process Validation using Formal Methods. In: Maximilien, M., Vallecillo, A., Wang, J. (Eds.), Service-Oriented Computing. ICSOC 2017. 15th International Conference on Service Oriented Computing (ICSOC 2017), 13-16 Nov 2017, Malaga, Spain."](https://openaccess.city.ac.uk/id/eprint/17900/)

The basic idea in the Translator (inside PrismTranslate/src/Translator/) is to produce a [PRISM model-checker](https://www.prismmodelchecker.org/) model from a certification process description and along with that also produce the corresponding code to run that certification process (using Lisp & Java).
