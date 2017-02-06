function gregorian_to_jalali(d) {
    var jalali = jd_to_persian(gregorian_to_jd(d[0], d[1], d[2]));
    jalali[1]--;
    return new JalaliDate(jalali[0], jalali[1], jalali[2]);
}
